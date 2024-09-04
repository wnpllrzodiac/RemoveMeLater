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

access_token = _get_access_token()
print(access_token)