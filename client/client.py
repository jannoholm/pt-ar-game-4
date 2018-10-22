from tkinter import *

import socket
import logging
import json

TCP_IP = "localhost"
TCP_PORT = 1337
BUFFER_SIZE = 1024

WIDTH = 1024
HEIGHT = 616

NORM_WIDTH = 1024
NORM_HEIGHT = 616

SEND_MESSAGES = True


class FieldCanvas (Frame):
    def __init__(self, master, width, height, sender, send_messages=True):
        self.master = master
        Frame.__init__(self, master)

        self.width = width
        self.height = height

        self.send_messages = send_messages
        if send_messages:
            self.sender = sender
            self.sender.connect()

        self.canvas = Canvas(self.master, width=width, height=height)

        self.canvas.pack()


        self.canvas.bind("<ButtonPress-1>", self.mouse_down)
        self.canvas.bind("<ButtonRelease-1>", self.mouse_up)

        self.static_rectangles = []
        self.rectangles = []

        self.selected_object = NONE

    def mouse_down(self, event):
        x = event.x
        y = event.y
        for rect in self.rectangles:
            if rect.is_hover(x, y):
                self.selected_object = rect
                event.widget.bind("<Motion>", self.motion)

    def mouse_up(self, event):
        event.widget.unbind("<Motion>")
        data = json.dumps(self.get_normalized_coordinates(NORM_WIDTH, NORM_HEIGHT))
        if self.send_messages:
            self.sender.send(data)

    def motion(self, event):
        self.canvas.coords(self.selected_object.object, self.selected_object.calculate_new_loc(event.x, event.y))
        data = json.dumps(self.get_normalized_coordinates(NORM_WIDTH, NORM_HEIGHT))
        import time
        time.sleep(0.032)
        if self.send_messages:
            self.sender.send(data)

    def get_object_coordinates(self):
        res = {}
        for rect in self.rectangles:
            res[rect.id] = (rect.location())
        return res

    def get_normalized_coordinates(self, new_x_size, new_y_size):
        res = {}
        ob = self.get_object_coordinates()
        for obj in ob:
            new_x = float(new_x_size) / self.width * ob[obj][0]
            new_y = float(new_y_size) / self.height * ob[obj][1]
            res[obj] = (int(new_x), int(new_y))
        return res

    def add_movable_rectangle(self, rectangle):
        self.rectangles.append(FieldObject(self.canvas,
                                           rectangle.name,
                                           rectangle.y,
                                           rectangle.x,
                                           rectangle.size,
                                           rectangle.color))

    def add_static_rectangle(self, name, x, y, color, size=50):
        self.static_rectangles.append(FieldObject(self.canvas, name, y, x, size, color))


class FieldObject:
    def __init__(self, canvas, identifier, center_x, center_y, size, color):
        self.canvas = canvas
        self.center_x = center_x
        self.center_y = center_y
        self.size = size
        self.color = color
        self.object = None
        self.create_box()
        self.id = identifier

    def create_box(self):
        self.object = self.canvas.create_rectangle(self.calculate_location(self.center_x, self.size, True),
                                                   self.calculate_location(self.center_y, self.size, True),
                                                   self.calculate_location(self.center_x, self.size, False),
                                                   self.calculate_location(self.center_y, self.size, False),
                                                   fill=self.color)

    @staticmethod
    def calculate_location(location, size, shift):
        if shift:
            return location + size/2
        return location - size/2

    def calculate_new_loc(self, new_x, new_y):
        return (self.calculate_location(new_x, self.size, True),
                self.calculate_location(new_y, self.size, True),
                self.calculate_location(new_x, self.size, False),
                self.calculate_location(new_y, self.size, False))

    def location(self):
        coord_list = self.canvas.coords(self.object)
        return (coord_list[0] + coord_list[2]) / 2, (coord_list[1] + coord_list[3]) / 2

    def is_hover(self, x, y):
        coords_list = self.canvas.coords(self.object)
        if coords_list[2] > x > coords_list[0] and coords_list[3] > y > coords_list[1]:
            return True
        return False


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


class Rectangle:
    def __init__(self, name, x, y, color, size=20):
        self.name = name
        self.x = x
        self.y = y
        self.color = color
        self.size = size


class CameraEmulator:
    def __init__(self, rectangles):

        logging.basicConfig(level=logging.INFO)

        self.root = Tk()
        self.root.title("Game Field")
        self.root.resizable(False, False)
        self.tcp_sender = TcpSender(TCP_IP, TCP_PORT, BUFFER_SIZE)

        self.field_canvas = FieldCanvas(self.root, WIDTH, HEIGHT, self.tcp_sender, send_messages=SEND_MESSAGES)



        ##self.field_canvas.canvas.pack()

        ## BG IMAGE
        bg = PhotoImage(file='background.png')
        self.field_canvas.canvas.create_image(0, -40, image=bg, anchor=NW)

        self.root.protocol("WM_DELETE_WINDOW", self.on_close)

        for rect in rectangles:
            self.field_canvas.add_movable_rectangle(rect)

        mainloop()

    def on_close(self):
        self.tcp_sender.disconnect()
        self.root.destroy()


redTeam = [Rectangle("0", 100, 100, "red"), Rectangle("1", 300, 100, "red")]
blueTeam = [Rectangle("2", 100, 400, "blue"), Rectangle("3", 300, 400, "blue")]
emulator = CameraEmulator(blueTeam)
