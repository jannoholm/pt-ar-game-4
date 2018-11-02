import cv2
from cv2 import aruco
import numpy as np

import socket
import logging
import json

import glob


def to_tuple(a):
    try:
        return tuple(to_tuple(i) for i in a)
    except TypeError:
        return a


def get_string(id, dst):
    return "ID: {}({}m)".format(id, round(dst, 2))


wid = 1920
hei = 1080

cam_id = 1

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


logging.basicConfig(level=logging.INFO)

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

while True:
    ret, frame = cap.read()

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    aruco_dict = aruco.Dictionary_get(aruco.DICT_6X6_250)

    parameters = aruco.DetectorParameters_create()
    corners, ids, rejectedImgPoints = aruco.detectMarkers(gray, aruco_dict, parameters=parameters)

    font = cv2.FONT_HERSHEY_SIMPLEX  # font for displaying text (below)

    if np.all(ids != None):
        rvec, tvec, _ = aruco.estimatePoseSingleMarkers(corners, 0.05, mtx, dist)

        dist_vec = tvec[0][0] * 0.714 * 1000

        print(dist_vec)

        sender.send(json.dumps({"1": (dist_vec[0], dist_vec[2])}))

    cv2.imshow('frame', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

sender.disconnect()
cap.release()
cv2.destroyAllWindows()