#!/bin/python3

# https://stackoverflow.com/questions/54660531/udp-sockets-with-python
# https://pyshine.com/Send-video-over-UDP-socket-in-Python/

import cv2
import base64
import socket

# constants
IP = '127.0.0.1'
PORT = 9999
BUFF_SIZE = 65536

# socket and socket binding
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind((IP, PORT))

# capturing video
capture = cv2.VideoCapture(0)
capture.set(cv2.CAP_PROP_FRAME_WIDTH, 960)
capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 540)
_, frame = capture.read()

while True:
    data, addr = s.recvfrom(BUFF_SIZE)      # receiving data from socket

    # received 'getFrame'
    if data == b'getFrame':
        _, frame = capture.read()   # reading frame

        # encoding frame into bytes
        encoded, buffer = cv2.imencode(
            '.jpg', frame, [cv2.IMWRITE_JPEG_QUALITY, 90])
        
        s.sendto(buffer, addr)     # sending frame

# TODO end of loop
# s.close()
# capture.release()
