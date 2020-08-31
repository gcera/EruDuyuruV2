package gcera.app.eruduyuru.objects

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import gcera.app.eruduyuru.receivers.AnnounceReceiver
import gcera.app.eruduyuru.receivers.CheckAnnounceWork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import java.util.concurrent.TimeUnit

object Alarm {
    /* @ExperimentalCoroutinesApi
     fun setCheckLastAnnounceAlarm(context: Context, isActive:Boolean, time:Int){
         val alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         val pendingIntent=PendingIntent.getBroadcast(
             context,
             Constants.CHECK_LAST_ANNOUNCE_REQUEST_CODE,
             Intent(context,AnnounceReceiver::class.java).apply {
                 action=Constants.ACTION_CHECK_LAST_ANNOUNCE
             },
             PendingIntent.FLAG_UPDATE_CURRENT
         )
        if (isActive){
            val repeatInterval=when(time){
                0->AlarmManager.INTERVAL_FIFTEEN_MINUTES
                1->AlarmManager.INTERVAL_HALF_HOUR
                2->AlarmManager.INTERVAL_HOUR
                3->AlarmManager.INTERVAL_HOUR*2
                else->AlarmManager.INTERVAL_HALF_DAY
            }
            val triggerTime=SystemClock.elapsedRealtime()+repeatInterval
            alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent
            )
        }else{
            alarmManager.cancel(pendingIntent)
        }
     }
 */
    @ExperimentalCoroutinesApi
    fun setCheckMealAlarm(context: Context, isActive: Boolean, mealTime: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.CHECK_MEAL_REQUEST_CODE,
            Intent(context, AnnounceReceiver::class.java).apply {
                action = Constants.ACTION_CHECK_MEAL
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (isActive) {

            val triggerTime = when (mealTime) {
                1 -> Calendar.getInstance(Locale.getDefault()).apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 14)
                    set(Calendar.MINUTE, 58)
                    set(Calendar.SECOND, 0)
                }
                else -> Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 10)
                    set(Calendar.MINUTE, 30)
                    set(Calendar.SECOND, 0)
                }
            }

            if (triggerTime.before(Calendar.getInstance())) {
                triggerTime.add(Calendar.DATE, 1)
            }
            println(triggerTime.time.toString())

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    @ExperimentalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.N)
    fun setCheckLastAnnounceAlarmWithWorkManager(context: Context, isActive: Boolean, time: Int) {
        if (isActive) {
            val repeatInterval = when (time) {
                0 -> AlarmManager.INTERVAL_FIFTEEN_MINUTES
                1 -> AlarmManager.INTERVAL_HALF_HOUR
                2 -> AlarmManager.INTERVAL_HOUR
                3 -> AlarmManager.INTERVAL_HOUR * 2
                else -> AlarmManager.INTERVAL_HALF_DAY
            }
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setTriggerContentMaxDelay(10, TimeUnit.MINUTES).build()
            val workRequest =
                PeriodicWorkRequestBuilder<CheckAnnounceWork>(repeatInterval, TimeUnit.MILLISECONDS)
                    .setConstraints(constraints)
                    .setInitialDelay(10, TimeUnit.MINUTES).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "CheckAnnounce",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}