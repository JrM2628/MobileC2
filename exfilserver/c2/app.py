from flask import request
from base64 import b64decode
from c2 import app

@app.route('/out', methods=['POST'])
def out():
    if request.method == 'POST':
        print("UUID: " + request.form.get("uuid"))
        print("DATA: " + b64decode(request.form.get("data")).decode())
    return "ok"