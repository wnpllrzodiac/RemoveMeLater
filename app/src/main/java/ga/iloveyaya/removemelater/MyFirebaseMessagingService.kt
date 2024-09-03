package ga.iloveyaya.removemelater

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Calendar

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)
        val mBuilder = NotificationCompat.Builder(this)
        mBuilder.setSmallIcon(R.mipmap.sym_def_app_icon)

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "getNotification IN")
            val notifReceived = remoteMessage.notification
            Log.d(TAG, "Message Notification Body: " + notifReceived!!.body)
            mBuilder.setContentTitle(notifReceived.title)
            mBuilder.setContentText(notifReceived.body)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(
                TAG, "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )
        }

        // Creates an explicit intent for the MainActivity
        val resultIntent = Intent(this, MainActivity::class.java)

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity::class.java)
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent: PendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // mId allows you to update the notification later on.
        val mId = Calendar.getInstance().time.time.toInt()
        mNotificationManager.notify(mId, mBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
    }

    companion object {
        private const val TAG = "Message"
    }
}