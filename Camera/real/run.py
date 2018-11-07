import cv2
from cv2 import aruco
import numpy as np

import socket
import logging
import json

import glob

import time
import math

import argparse
from builtins import int
from numpy import double

def to_tuple(a):
    try:
        return tuple(to_tuple(i) for i in a)
    except TypeError:
        return a


def get_string(id, dst):
    return "ID: {}({}m)".format(id, round(dst, 2))

parser = argparse.ArgumentParser(description='Run token detection via webcam')
parser.add_argument('-c','--camera', help='Camera index to use, usually starting from zero', required=True, type=int)
parser.add_argument('-a','--angle', help='Angle in degrees from top TV edge to camera mid axis', required=True, type=double)
parser.add_argument('-x','--boardX', help='Camera X location in mm compared to zero point of TV top left corner, top side is X axis going right', required=True, type=double)
parser.add_argument('-y','--boardY', help='Camera Y location in mm compared to zero point of TV top left corner, left side is Y axis going down', required=True, type=double)
args = parser.parse_args()

physical_camera_angle = np.radians( args.angle )
physical_camera_x = args.boardX
physical_camera_y = args.boardY

cam_id = args.camera

TCP_IP = "localhost"
TCP_PORT = 1337
BUFFER_SIZE = 1024

calibrate = False

sample_size = 20


cap = cv2.VideoCapture(cam_id)

#cap.set(cv2.CAP_PROP_FRAME_WIDTH, wid)
#cap.set(cv2.CAP_PROP_FRAME_HEIGHT, hei)



path = ""

aruco_dict = aruco.getPredefinedDictionary(aruco.DICT_6X6_1000)

##cm
markel_side = 3.75

marker_separation = 0.5

board = aruco.GridBoard_create(4, 5, markel_side, marker_separation, aruco_dict)

img = board.draw((864, 1080))

#cv2.imshow("aruco", img)

criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

objp = np.zeros((6*7,3), np.float32)
objp[:, :2] = np.mgrid[0:7, 0:6].T.reshape(-1, 2)

# Arrays to store object points and image points from all the images.
objpoints = [] # 3d point in real world space
imgpoints = [] # 2d points in image plane.


images = glob.glob('calib/*.jpg')

aruco_params = aruco.DetectorParameters_create()
first = True
counter = []
corners_list = []
id_list = []

def calibrate_camera(cam_x, cam_y, physical_camera_x, physical_camera_y):
    
    # Basic math, right? https://gamedev.stackexchange.com/questions/18340/get-position-of-point-on-circumference-of-circle-given-an-angle
    
    distance_from_camera = math.sqrt(cam_x**2 + cam_y**2)
    physical_distance_from_camera = math.sqrt((1440/2 - physical_camera_x)**2 + (800/2 - physical_camera_y)**2)
    
    # Creates the factor offset at least correctly in the middle point
    calibration_distance_factor = physical_distance_from_camera / distance_from_camera if distance_from_camera != 0 else 1
    
    # cam_x: -90 cam_y 816 physical_camera_x 280.0 physical_camera_y 809.0
    # Angle between camera Y and target object in radians, same as camera_angle in the main flow
    # Tan(angle) = Opposite / Adjacent
    calibration_angle_offset = np.tan(cam_x / cam_y) if cam_y != 0 else 0    
    
    return (calibration_distance_factor, calibration_angle_offset)
    
def remap_points(cam_x, cam_y, physical_camera_x, physical_camera_y, calibration_distance_factor, calibration_angle_offset):
    
    # Basic math, right? https://gamedev.stackexchange.com/questions/18340/get-position-of-point-on-circumference-of-circle-given-an-angle

    print("cam_x:", cam_x, "cam_y", cam_y, "physical_camera_x", physical_camera_x, "physical_camera_y", physical_camera_y)

    distance_camera = math.sqrt(cam_x**2 + cam_y**2) * calibration_distance_factor
	
    # Angle between camera Y and target object in radians
    # Tan(angle) = Opposite / Adjacent
    camera_angle = np.tan(cam_x / cam_y) if cam_y != 0 else 0 
    
    # Angle between board X (top edge) and object, note that board angle increases to towards bottom, but camera angle increases towards top
    board_angle = physical_camera_angle + camera_angle + calibration_angle_offset
    
    # Distance from top left corner to object
    # x = Cos(a) * r
    board_x = physical_camera_x + np.cos( board_angle ) * distance_camera
    # y = Sin(a) * r
    board_y = physical_camera_y + np.sin( board_angle ) * distance_camera
    
    print("distance_camera: ", round(distance_camera), "camera_angle: ", round(np.degrees(camera_angle)), "board_angle: ", round(np.degrees(board_angle)), "board_x: ", round(board_x), "board_y", round(board_y))
	
    return (board_x, board_y)

class TcpSender:
    def __init__(self, ip, port, buffer):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.ip = ip
        self.port = port
        self.buffer = buffer
        self.log = logging.getLogger('TcpSender')

    def connect(self):
        self.log.info('Connecting to: %s:%s', self.ip, self.port)
        self.socket.connect((self.ip, self.port))

    def send(self, message):
        self.log.info('Sending: %s', message)
        self.socket.send(message.encode('utf-8'))

    def read(self):
        resp = self.socket.recv(self.buffer)
        self.log.info('Got message: %s', resp)
        return resp

    def disconnect(self):
        self.log.info('Disconnecting from: %s:%s', self.ip, self.port)
        self.socket.close()


logging.basicConfig(level=logging.WARNING)

sender = TcpSender(TCP_IP, TCP_PORT, BUFFER_SIZE)
sender.connect()

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
print("Calibrating camera .... Please wait...")
# mat = np.zeros((3,3), float)
ret, mtx, dist, rvecs, tvecs = aruco.calibrateCameraAruco(corners_list, id_list, counter, board, img_gray.shape, None, None)
print("Calibrating camera complete")

aruco_dict = aruco.Dictionary_get(aruco.DICT_ARUCO_ORIGINAL)


nrOfLoops = 0
calibration = (1,0)

while True:
    time.sleep(0.2)

    ret, frame = cap.read()

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    parameters = aruco.DetectorParameters_create()
    corners, ids, rejectedImgPoints = aruco.detectMarkers(gray, aruco_dict, parameters=parameters)

    font = cv2.FONT_HERSHEY_SIMPLEX  # font for displaying text (below)

    if np.all(ids != None):
        sent_markers = []
        for corner in range(0, len(corners)):
            marker_id = ids[corner]
            marker_corners = corners[corner]
            rvec, tvec, _ = aruco.estimatePoseSingleMarkers(marker_corners, 0.05, mtx, dist)
            if marker_id in sent_markers:
                continue
            dist_vec = tvec[0][0] * 0.714 * 1000

            # print(dist_vec)
            #marker_distance = math.sqrt(int(dist_vec[0])**2 + int(dist_vec[2])**2)
            if nrOfLoops < 40: # 10 seconds
                #print("CAL:", calibrate_camera(int(dist_vec[0]), int(dist_vec[2]), physical_camera_x, physical_camera_y))
                calibration = calibrate_camera(int(dist_vec[0]), int(dist_vec[2]), physical_camera_x, physical_camera_y)
                print(calibration)
            else:
                coordinates = remap_points(int(dist_vec[0]), int(dist_vec[2]), physical_camera_x, physical_camera_y, calibration[0], calibration[1])
                print(coordinates)
                sender.send(json.dumps({str(marker_id[0]): (int(coordinates[0]), int(coordinates[1]))}))    
            
            nrOfLoops += 1;
            
            time.sleep(0.05)
            sent_markers.append(marker_id)

    cv2.imshow('frame', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

sender.disconnect()
cap.release()
cv2.destroyAllWindows()