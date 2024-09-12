import cherrypy
import random
import string

import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
import google
from google.oauth2 import service_account

mytoken = 'cPxxxx'

cred = credentials.Certificate("xxx.json")
default_app = firebase_admin.initialize_app(credential=cred)

class HelloWorld(object):
    @cherrypy.expose
    def index(self):
        return "Hello World!"
    index.exposed = True
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def sendmsg(self, title='fcm msg', body=''):
        # curl -X POST http://127.0.0.1:9965/sendmsg -d "title=imtitle&body=imbodybody"
        
        message = messaging.Message(
            data = {
                'title': title,
                'body': body,
                "fromNotification":"true",
                'key1': 'value1',
                'key2': 'value2'
            },
            token=mytoken,
            android=messaging.AndroidConfig(
                priority='high'
            )
        )

        # Send the message
        response = messaging.send(message)
        # Response is a message ID string.
        print('Successfully sent message:', response)
        
        return {"code": 0, 'msg': 'fcm msg sent.', 'title': title, 'body': body}
    
    @cherrypy.expose
    def generate(self, length=8):
        return ''.join(random.sample(string.hexdigits, int(length)))

cherrypy.config.update({'server.socket_port': 9965})
cherrypy.quickstart(HelloWorld())