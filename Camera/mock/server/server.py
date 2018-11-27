import asyncore
import collections
import socket
import json
import requests
import numpy as np
import threading

import traceback

import logging

MAX_MESSAGE_LENGTH = 8000
PORT = 1337

POSITION_POST_URL = "http://10.67.94.159:8101/control/position"
QR_POST_URL = "http://10.67.94.159:8101/control/user"

AVG_SAMPLES = 10

id_side_map = {1: "1", 2: "1", 3: "2", 4: "2"}


class RemoteClient(asyncore.dispatcher):

    def __init__(self, host, socket, address):
        asyncore.dispatcher.__init__(self, socket)
        self.host = host
        self.outbox = collections.deque()

    def say(self, message):
        self.outbox.append(message)

    def handle_read(self):
        client_message = self.recv(MAX_MESSAGE_LENGTH)
        self.host.broadcast(client_message)

    def handle_write(self):
        if not self.outbox:
            return
        message = self.outbox.popleft()
        if len(message) > MAX_MESSAGE_LENGTH:
            raise ValueError('Message too long')
        self.send(message)

    def writable(self):
        return False


class Server(asyncore.dispatcher):

    log = logging.getLogger('Host')

    def __init__(self, address=('localhost', PORT), func=None):
        self.log.info("Init async server")
        asyncore.dispatcher.__init__(self)
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.bind(address)
        self.listen(5)
        self.remote_clients = []
        self.current_state = {}
        self.marker_buffer = {}
        self.send_thread = None

    def handle_accept(self):
        socket, addr = self.accept()
        self.log.info('Accepted client at %s', addr)
        self.remote_clients.append(RemoteClient(self, socket, addr))

    def handle_read(self):
        self.log.info('Received message: %s', self.read())

    def broadcast(self, message):
        self.log.debug('Broadcasting message: %s', message)
        for remote_client in self.remote_clients:
            if len(message) > 0:
                try:
                    split_char = '}'
                    res = message.decode('utf-8')
                    split_res = [e + split_char for e in res.split(split_char) if e]

                    for el in split_res:
                        try:
                            loaded_response = json.loads(el)

                            if "qr_data" in loaded_response:
                                self.log.debug("QR CODE RECIEVED: %s", loaded_response)
                                self.post_qr(loaded_response["qr_data"], id_side_map[loaded_response["camera_id"]])
                                continue

                            self.log.debug('Received message : %s', loaded_response)
                            self.log.debug('Current state: %s', self.current_state)
                            diff_resp = self.calculate_diff(loaded_response)

                            self.append_buffer(diff_resp)
                            self.current_state.update(loaded_response)
                        except:
                            self.log.debug("Failed to parse element: %s", el)
                except:
                    tb = traceback.format_exc()
                    print(tb)
                    pass
            else:
                self.remote_clients.remove(remote_client)

    def send_average_and_clear(self):
        avg = self.average_list(self.marker_buffer)
        self.log.info("Autosending: %s", avg)
        if avg:
            #self.clear_buffer()
            #self.post_date(avg)
            pass

    @staticmethod
    def average_list(samples):
        res = {}
        for key, value in samples.items():
            res[key] = np.mean(np.array(value), axis=0).astype(int).tolist()
        return res

    def append_buffer(self, results):
        for key, value in results.items():
            new_list = self.marker_buffer.get(key, [])
            if len(new_list) < AVG_SAMPLES:
                new_list.append(value)
            else:
                new_list.insert(0, value)
                new_list.pop()
            self.marker_buffer[key] = new_list

    def clear_buffer(self):
        self.marker_buffer = {}

    def calculate_diff(self, messages):
        output = {}
        compare = lambda x, y: collections.Counter(x) == collections.Counter(y)
        for key, value in messages.items():
            if key in self.current_state:
                if compare(value, self.current_state[key]):
                    self.log.debug('Value %s equals %s', value, self.current_state[key])
                else:
                    self.log.debug('%s != %s', value, self.current_state[key])
                    output[key] = value
            else:
                output[key] = value
        return output

    def post_date(self, data_map):
        for key, value in data_map.items():
            data_dump = self.data_to_url_encoded(key, value)
            self.log.info('POST DATA: %s', data_dump)
            r = requests.post(url=POSITION_POST_URL, data=data_dump)
            self.log.info('Response: %s', r.text)

    def post_qr(self, qr_data, side):
        data = {"qrCode": qr_data, "position": side}
        self.log.info('POST DATA: %s', data)
        r = requests.post(url=QR_POST_URL, data=data)
        self.log.info('Response: %s', r.text)

    def data_to_json(self, key, data):
        out_data = {}
        out_data["qrCode"] = key
        out_data["x"] = data[0]
        out_data["y"] = data[1]
        return json.dumps(out_data)

    def data_to_url_encoded(self, key, data):
        out = {}
        out["qrCode"] = key
        out["x"] = data[0]
        out["y"] = data[1]
        return out
        #return "qrCode={}&x={}&y={}".format(key, , )


def thread():
    host.send_average_and_clear()
    threading.Timer(0.2, thread).start()


logging.basicConfig(level=logging.DEBUG)
logging.info('Creating server')
host = Server()
thread()
logging.info('Looping')
asyncore.loop()
