import cv2
from cv2 import aruco
import numpy as np

wid = 1280
hei = 720

cap = cv2.VideoCapture(1)
#cap.set(cv2.CAP_PROP_FRAME_WIDTH, wid)
#cap.set(cv2.CAP_PROP_FRAME_HEIGHT, hei)

print(cap)

count = 0

path = "calib/"

while True:
    ret, frame = cap.read()

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    cv2.imshow('frame', gray)

    if cv2.waitKey(1) & 0xFF == ord('c'):
        name = path + str(count) + ".jpg"
        count += 1
        cv2.imwrite(name, gray)
        print("image saved")

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()