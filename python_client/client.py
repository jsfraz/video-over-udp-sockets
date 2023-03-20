#!/bin/python3

# https://pyshine.com/Send-video-over-UDP-socket-in-Python/
# https://numpy.org/devdocs/reference/generated/numpy.fromstring.html

import cv2
import socket
import base64
import numpy as np

# constants
IP = '127.0.0.1'
PORT = 9999
BUFF_SIZE = 65536

# sascade
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_alt.xml')

# socket and socket binding
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

while True:
    # sending 'getFrame' and receiving bytes
    s.sendto(b'getFrame', (IP, PORT))
    received, addr = s.recvfrom(BUFF_SIZE)

    
    # encoding bytes info frame
    img_array = np.asarray(bytearray(received), dtype=np.uint8)
    frame = cv2.imdecode(img_array, 1)

    # detecting faces
    faces = face_cascade.detectMultiScale(frame, 1.1, 4)
    # Draw the rectangle around each face
    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 0, 255), 2)

    # showing image
    cv2.imshow('Received video', frame)

    # exiting
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

s.close()
cv2.destroyAllWindows()
