package gcera.app.eruduyuru.objects

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import gcera.app.eruduyuru.receivers.AnnounceReceiver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

object Alarm {
    @ExperimentalCoroutinesApi
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
           val testRepeat=30*1000
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

    @ExperimentalCoroutinesApi
    fun setCheckMealAlarm(context: Context, isActive:Boolean, mealTime:Int){
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
            val triggerTime = when(mealTime){
                1-> Calendar.getInstance().apply {
                    timeInMillis=System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY,16)
                    set(Calendar.MINUTE,30)
                    set(Calendar.SECOND,0)
                }
                else -> Calendar.getInstance().apply {
                    timeInMillis=System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY,10)
                    set(Calendar.MINUTE,30)
                    set(Calendar.SECOND,0)
                }
            }
            if (triggerTime.before(Calendar.getInstance()))
                triggerTime.add(Calendar.DATE,1)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }else{
            alarmManager.cancel(pendingIntent)
        }
    }
}