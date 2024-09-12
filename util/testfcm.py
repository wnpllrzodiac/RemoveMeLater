import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
import google
from google.oauth2 import service_account

SCOPES = ['https://www.googleapis.com/auth/firebase.messaging']

def _get_access_token():
  """Retrieve a valid access token that can be used to authorize requests.

  :return: Access token.
  """
  credentials = service_account.Credentials.from_service_account_file(
    'XXX.json', scopes=SCOPES)
  request = google.auth.transport.requests.Request()
  credentials.refresh(request)
  return credentials.token

mytoken = 'TOKEN'

cred = credentials.Certificate("XXX.json")
default_app = firebase_admin.initialize_app(credential=cred)
 
message = messaging.Message(
    # onMessageReceived not triggered when notification sent to android and app is running background
    #notification=messaging.Notification(
    #    title='This is a Notification Title 1111',
    #    body='This is a Notification Body 2222',
    #),
    data = {
        'title' :'This is a Notification Title 1111',
        'body': 'This is a Notification Body 2222',
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

access_token = _get_access_token()
print(access_token)