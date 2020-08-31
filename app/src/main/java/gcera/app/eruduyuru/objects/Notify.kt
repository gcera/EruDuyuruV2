package gcera.app.eruduyuru.objects

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import gcera.app.eruduyuru.ui.activities.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

object Notify {
    @ExperimentalCoroutinesApi
    fun sendNotification(context: Context,
                         content: String,
                         departName: String,
                         indexUrl:String,
                         indexSelect:String,
                         imgId:Int
    ) {
        val notifyManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifyBuilder = NotificationCompat.Builder(context, Constants.NOTIFY_CHANNEL_ID)
            .apply {
                setContentTitle(departName)
                setContentText(content)
                priority = NotificationCompat.PRIORITY_HIGH
                setDefaults(Notification.DEFAULT_SOUND)
                setAutoCancel(true)
                setVibrate(longArrayOf(0L, 500L, 100L, 700L))
                setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        Constants.NOTIFY_REQUEST_CODE,
                        Intent(context,MainActivity::class.java)
                            .apply {
                            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            action=Constants.ACTION_NAVIGATE_DETAIL
                            putExtra(Constants.KEY_NOTIFY_TITLE,content)
                            putExtra(Constants.KEY_NOTIFY_INDEXURL,indexUrl)
                            putExtra(Constants.KEY_NOTIFY_INDEX_SELECT,indexSelect)
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                setSmallIcon(imgId)
            }
        notifyManager.notify(60, notifyBuilder.build())
    }
}