import cv2
from cv2 import aruco
import numpy as np

import socket
import logging
from logging.handlers import RotatingFileHandler

import glob
import json

import math
import time

import configparser
import argparse
from builtins import int
from numpy import double

from pyzbar.pyzbar import ZBarSymbol, decode


debug = False



def to_tuple(a):
    try:
        return tuple(to_tuple(i) for i in a)
    except TypeError:
        return a


def get_string(id, dst):
    return "ID: {}({}m)".format(id, round(dst, 2))


parser = argparse.ArgumentParser(description='Run token detection via webcam')
parser.add_argument('-c', '--camera', help='Camera index to use, usually starting from zero', required=True, type=int)

args = parser.parse_args()

physical_c_angle = 0
physical_c_x = 0
physical_c_y = 0

cam_id = args.camera

log_file = "camera_log_" + str(cam_id) + ".log"
open(log_file, 'a').close()

log_formatter = logging.Formatter('%(asctime)s %(levelname)s %(funcName)s(%(lineno)d) %(message)s')
handler = RotatingFileHandler(log_file, mode='a', maxBytes=20*1024*1024, 
                                 backupCount=2, encoding=None, delay=0)
handler.setFormatter(log_formatter)
handler.setLevel(logging.INFO)

log = logging.getLogger('root')
log.setLevel(logging.INFO)

log.addHandler(handler)

frame_name = 'frame' + str(cam_id)

TCP_IP = "localhost"
TCP_PORT = 1337
BUFFER_SIZE = 1024

calibrate = False

cap = cv2.VideoCapture(cam_id)

path = ""

aruco_dict = aruco.getPredefinedDictionary(aruco.DICT_6X6_1000)

##cm
markel_side = 2.5

marker_separation = 0.5

board = aruco.GridBoard_create(4, 5, markel_side, marker_separation, aruco_dict)

img = board.draw((864, 1080))

criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

objp = np.zeros((6 * 7, 3), np.float32)
objp[:, :2] = np.mgrid[0:7, 0:6].T.reshape(-1, 2)

# Arrays to store object points and image points from all the images.
objpoints = []  # 3d point in real world space
imgpoints = []  # 2d points in image plane.

images = glob.glob('calib/*.jpg')

aruco_params = aruco.DetectorParameters_create()
first = True
counter = []
corners_list = []
id_list = []


def remap_points(cam_x, cam_y, physical_camera_x, physical_camera_y, calibration_distance_factor,
                 calibration_angle_offset, physical_camera_angle):
    # Basic math, right? https://gamedev.stackexchange.com/questions/18340/get-position-of-point-on-circumference-of-circle-given-an-angle

    #print("cam_x:", cam_x, "cam_y", cam_y, "physical_camera_x", physical_camera_x, "physical_camera_y",
    #      physical_camera_y)

    distance_camera = math.sqrt(cam_x ** 2 + cam_y ** 2) * calibration_distance_factor

    # Angle between camera Y and target object in radians
    # Tan(angle) = Opposite / Adjacent
    camera_angle = np.tan(cam_x / cam_y) if cam_y != 0 else 0

    # Angle between board X (top edge) and object, note that board angle increases to towards bottom, but camera angle increases towards top
    board_angle = physical_camera_angle + camera_angle + calibration_angle_offset

    # Distance from top left corner to object
    # x = Cos(a) * r
    board_x = physical_camera_x + np.cos(board_angle) * distance_camera
    # y = Sin(a) * r
    board_y = physical_camera_y + np.sin(board_angle) * distance_camera

    log.debug("distance_camera: ", round(distance_camera), "camera_angle: ", round(np.degrees(camera_angle)),
          "board_angle: ", round(np.degrees(board_angle)), "board_x: ", round(board_x), "board_y", round(board_y))

    return (board_x, board_y)


class TcpSender:
    def __init__(self, ip, port, buffer, handler):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.ip = ip
        self.port = port
        self.buffer = buffer
        self.log = logging.getLogger('TcpSender')
        self.log.addHandler(handler)

    def is_connected(self):
        self.socket.recv()

    def connect(self):
        self.log.info('Connecting to: %s:%s', self.ip, self.port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect((self.ip, self.port))

    def send(self, message):
        self.log.info('Sending: %s', message)
        try:
            self.socket.send(message.encode('utf-8'))
        except:
            try:
                self.disconnect()
                self.connect()
            except:
                pass

    def read(self):
        resp = self.socket.recv(self.buffer)
        self.log.info('Got message: %s', resp)
        return resp

    def disconnect(self):
        self.log.info('Disconnecting from: %s:%s', self.ip, self.port)
        self.socket.close()


class Configuration:
    def __init__(self, camera_id, handler):
        self.log = logging.getLogger('Configuration')
        self.log.addHandler(handler)
        self.log.info("Loading configuration")
        self.filename = str(camera_id) + "_configuration.cfg"
        self.create_file()
        self.config = configparser.RawConfigParser()
        self.config.read(self.filename)

    def get_param(self, group, field):
        return self.config.get(group, field)

    def set_param(self, group, field, value):
        self.config.set(group, field, value)
        with open(self.filename, 'w') as configfile:
            self.config.write(configfile)
            configfile.close()

    def create_file(self):
        try:
            file = open(self.filename, 'r')
            file.close()
        except IOError:
            file = open(self.filename, 'w')
            file.close()



while True:
    try:
        sender = TcpSender(TCP_IP, TCP_PORT, BUFFER_SIZE, handler)
        break
    except:
        pass
    time.sleep(1)


for fname in images:
    print(fname)
    img = cv2.imread(fname)
    img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    corners, ids, rejectedImgPoints = aruco.detectMarkers(img_gray, aruco_dict, parameters=aruco_params)
    if first:
        corners_list = corners
        print(type(corners))
        id_list = ids
        first = False
    else:
        corners_list = np.vstack((corners_list, corners))
        id_list = np.vstack((id_list, ids))
    counter.append(len(ids))

counter = np.array(counter)
log.info("Calibrating camera .... Please wait...")
# mat = np.zeros((3,3), float)
ret, mtx, dist, rvecs, tvecs = aruco.calibrateCameraAruco(corners_list, id_list, counter, board, img_gray.shape, None,
                                                          None)
log.info("Calibrating camera complete")

aruco_dict = aruco.Dictionary_get(aruco.DICT_4X4_50)

nrOfLoops = 0
calibration = (1, 0)


def nothing(x):
    pass


conf = Configuration(cam_id, handler)
conf.set_param('CAMERA', 'ID', int(cam_id))

cv2.namedWindow(frame_name)
cv2.createTrackbar('ANGLE', frame_name, 0, 360, nothing)
cv2.createTrackbar('X', frame_name, 0, 1900, nothing)
cv2.createTrackbar('Y', frame_name, 0, 1200, nothing)

cv2.setTrackbarPos('ANGLE', frame_name, int(conf.get_param('PARAMS', 'ANGLE')))
cv2.setTrackbarPos('X', frame_name, int(conf.get_param('PARAMS', 'X')))
cv2.setTrackbarPos('Y', frame_name, int(conf.get_param('PARAMS', 'Y')))

qr_count = 0
qr_sample_rate = 10

allowed_id = [0, 4]

while True:

    physical_c_angle = np.radians(cv2.getTrackbarPos('ANGLE', frame_name))
    physical_c_x = cv2.getTrackbarPos('X', frame_name)
    physical_c_y = cv2.getTrackbarPos('Y', frame_name)

    time.sleep(0.05)
    ret, frame = cap.read()

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    if qr_count > qr_sample_rate:
        decode_data = decode(gray, symbols=[ZBarSymbol.QRCODE])
        if decode_data:
            qr_data = decode_data[0][0].decode("UTF-8")
            sender.send(json.dumps({"qr_data": qr_data, "camera_id": cam_id}))
        qr_count = 0

    qr_count = qr_count + 1

    parameters = aruco.DetectorParameters_create()
    #print(parameters)

    font = cv2.FONT_HERSHEY_SIMPLEX  # font for displaying text (below)

    corners, ids, rejectedImgPoints = aruco.detectMarkers(gray, aruco_dict, parameters=parameters)
    rvec, tvec, _ = aruco.estimatePoseSingleMarkers(corners, markel_side, mtx, dist)
 
    if rvec is not None:
        for i in range(len(rvec)):
            if ids[i] in allowed_id:
 
                if debug:
                    frame = cv2.aruco.drawAxis(frame, mtx, dist, rvec[i], tvec[i], 0.1)
                    frame = cv2.aruco.drawDetectedMarkers(frame, corners, ids)
 
                marker_id = ids[i]
 
                marker_x = tvec[i][0][0] * 10
                marker_y = tvec[i][0][2] * 10
                marker_z = tvec[i][0][1] * 10

                if marker_z > 0:
                    continue
                if marker_z < -100:
                    continue
 
                coordinates = remap_points(marker_x,
                                           marker_y,
                                           physical_c_x - 200,
                                           physical_c_y - 200,
                                           calibration[0],
                                           calibration[1], physical_c_angle)
 
                sender.send(json.dumps(
                    {str(marker_id[0]): (int(coordinates[0] / 1440 * 10000), int(coordinates[1] / 800 * 10000))}))

    if debug:
        cv2.imshow(frame_name, frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

conf.set_param("PARAMS", "angle", int(np.degrees(physical_c_angle)))
conf.set_param("PARAMS", "x", int(physical_c_x))
conf.set_param("PARAMS", "y", int(physical_c_y))

sender.disconnect()
cap.release()
cv2.destroyAllWindows()
