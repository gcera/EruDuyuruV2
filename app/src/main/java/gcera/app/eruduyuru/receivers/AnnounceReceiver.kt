package gcera.app.eruduyuru.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.objects.Alarm
import gcera.app.eruduyuru.objects.AppData
import gcera.app.eruduyuru.objects.Constants
import gcera.app.eruduyuru.objects.Notify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class AnnounceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.ACTION_CHECK_MEAL)
            checkMeal(context)
    }

   /* private fun checkLastAnnounce(context: Context) {
        println(AnnounceReceiver::class.simpleName + " check started")
        val sharedPref =
            context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val requestNumber = sharedPref.getInt(Constants.KEY_DATA_INDEX, -1)
        val forMuh = sharedPref.getBoolean(Constants.KEY_FOR_MUH, false)
        println(requestNumber)
        if ((requestNumber != -1) and (isNetworkAvailable(context))) {
            val requestData = AppData.getRequestData(requestNumber, forMuh)
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder().url(requestData.url).build()
            val imgId = when {
                forMuh and (requestNumber != -1) -> AppData.MUH_LOGOLAR[requestNumber]
                !forMuh and (requestNumber != -1) -> AppData.FAKULTE_LOGOLAR[requestNumber]
                else -> R.drawable.ic_main
            }
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    val cssSelector = "${requestData.select0}1${requestData.select1}"
                    val elements = Jsoup.parse(response.body?.string()).select(cssSelector)
                    val title = elements.text()
                    val lastTitle = sharedPref.getString(Constants.KEY_LAST_ANNOUNCE, "") ?: ""
                    if (!lastTitle.let { title!!.contentEquals(it) }) {
                        sharedPref.edit().putString(Constants.KEY_LAST_ANNOUNCE, title).apply()
                        var url = response.request.url.toString()
                        if (url == "http://guzelsanat.erciyes.edu.tr/Duyurular") {
                            url = "http://guzelsanat.erciyes.edu.tr/"
                        }
                        if (url == "http://egitim.erciyes.edu.tr/announcement/1/1/") {
                            url = "http://egitim.erciyes.edu.tr"
                        }
                        val indexUrl = url + (elements.select("a").attr("href"))
                        Notify.sendNotification(
                            context,
                            title,
                            requestData.departName,
                            indexUrl,
                            requestData.indexSelect,
                            imgId
                        )
                    }//last announce =? new announce
                }
            })//enqueue
        }
    }*/

    private fun checkMeal(context: Context) {
        val requestData = AppData.getMealData()
        Notify.sendNotification(
            context,
            "Bugün yemekte ne var?",
            "Yemek Listesi",
            requestData.url,
            requestData.select0,
            R.drawable.ic_meal
        )
        val mealTime=  if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>12) 1 else 0
        println("setted meal time $mealTime")
        Alarm.setCheckMealAlarm(context,true,mealTime)
    }
}
