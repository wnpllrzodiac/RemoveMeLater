import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
 
mytoken = 'TOKEN'

cred = credentials.Certificate("XXX.json")
default_app = firebase_admin.initialize_app(credential=cred)
 
message = messaging.Message(
    notification=messaging.Notification(
        title='This is a Notification Title 1111',
        body='This is a Notification Body 2222',
    ),
    token=mytoken
)

# Send the message
response = messaging.send(message)
# Response is a message ID string.
print('Successfully sent message:', response)