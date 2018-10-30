import asyncore
import collections
import socket
import json
import requests

import logging

MAX_MESSAGE_LENGTH = 1024
PORT = 1337

POST_URL = "http://10.67.95.208:8101/control/position"


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


class Server(asyncore.dispatcher):

    log = logging.getLogger('Host')

    def __init__(self, address=('localhost', PORT)):
        asyncore.dispatcher.__init__(self)
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.bind(address)
        self.listen(1)
        self.remote_clients = []

        self.current_state = {}

    def handle_accept(self):
        socket, addr = self.accept()
        self.log.info('Accepted client at %s', addr)
        self.remote_clients.append(RemoteClient(self, socket, addr))

    def handle_read(self):
        self.log.info('Received message: %s', self.read())

    def broadcast(self, message):
        self.log.info('Broadcasting message: %s', message)
        for remote_client in self.remote_clients:
            if len(message) > 0:
                message_str = message.decode('utf-8')
                loaded_response = {}
                try:
                    loaded_response = json.loads(message_str)
                except:
                    self.log.error("Failed to parse response")
                self.log.debug('Received message : %s', loaded_response)
                self.log.debug('Current state: %s', self.current_state)

                diff_resp = self.calculate_diff(loaded_response)
                self.log.info('Sending: %s', diff_resp)

                self.post_date(diff_resp)

                self.current_state.update(loaded_response)
            else:
                self.remote_clients.remove(remote_client)

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
        data = {}
        data["data"] = data_map

        for key, value in data_map.items():
            data_dump = self.data_to_url_encoded(key, value)
            self.log.info('POST DATA: %s', data_dump)
            r = requests.post(url=POST_URL, data=data_dump)
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

logging.basicConfig(level=logging.INFO)
logging.info('Creating server')
host = Server()
logging.info('Looping')
asyncore.loop()
