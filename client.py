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

# socket and socket binding
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setblocking(False)

while True:
    received = b''

    # sending 'getFrame' and receiving bytes
    try:
        s.sendto(b'getFrame', (IP, PORT))
        received, addr = s.recvfrom(BUFF_SIZE)
    except:
        received = b'empty'

    # received data
    if received != b'empty':
        # encoding bytes info frame
        data = base64.b64decode(received, ' /')
        npdata = np.frombuffer(data, dtype=np.uint8)
        frame = cv2.imdecode(npdata, 1)

        # showing image
        cv2.imshow('Received video', frame)

        # exiting
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

cv2.destroyAllWindows()