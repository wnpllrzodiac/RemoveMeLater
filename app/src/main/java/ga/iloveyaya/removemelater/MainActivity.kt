package ga.iloveyaya.removemelater

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import ga.iloveyaya.removemelater.ui.theme.RemoveMeLaterTheme

class MainActivity : ComponentActivity() {
    private var TAG:String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.i(TAG, "fcm_token got: $token")
            findViewById<TextView>(R.id.tv_fire_token).text = token.toString()
            // cKDqlkENTmS1UEhqmRNRj8:APA91bHjm5KT7RbOEYRrQmtWa6lJzK6nNO6lv4CZ5QvUvs9yE0SvZr5JrTo84TR-eoUgKe90m9w5Rj7evhNiNc5ud5GpNMpaAgximYcDA8i7i3lGVny-0oGe39CxC65Z_tXasU5Qpehm
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

            // https://console.firebase.google.com/u/0/project/bibo-3622b/overview?hl=zh-cn
        })

        // Subscribes the application to the topics general
        FirebaseMessaging.getInstance().subscribeToTopic("general")

        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_copy_token).setOnClickListener {
            val tvToken = findViewById<TextView>(R.id.tv_fire_token)
            saveToClipboard(tvToken.text.toString())
            Toast.makeText(this, "TOKEN已粘贴", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getClipboardContent(): String? {
        // 获取系统剪贴板
        val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 返回数据
        val clipData: ClipData? = clipboard.primaryClip
        if (clipData == null || clipData.itemCount <= 0) {
            return ""
        }
        val item = clipData.getItemAt(0)
        /**
         *清空剪切板
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //要api28以上
            clipboard.clearPrimaryClip()
        } else {
            clipboard.setPrimaryClip(ClipData(null))
        }
        return if (item == null || item.text == null) {
            ""
        } else item.text.toString()
    }

    private fun saveToClipboard(text: String) {
        val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, text)
        clipboard.setPrimaryClip(clipData)
    }
    
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
