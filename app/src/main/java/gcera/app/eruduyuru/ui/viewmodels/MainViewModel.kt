package gcera.app.eruduyuru.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gcera.app.eruduyuru.models.Announce
import gcera.app.eruduyuru.models.RequestData
import gcera.app.eruduyuru.models.UserPref
import gcera.app.eruduyuru.objects.AppData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {
    sealed class ResponseState {
        object Success : ResponseState()
        object Fail : ResponseState()
        object Loading : ResponseState()
    }

    var title=""
    var indexUrl=""
    var indexSelect=""
    var mainAnnounceText=""
    var mainEruAnnounceText=""
    var mainEruNewsText=""
    var mainMealText=""
    val responseState = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val liveRequestData = MutableLiveData(AppData.getRequestData(1))
    val liveResponseData = MutableLiveData(mutableListOf<Announce>())
    var oldRequestData:RequestData?=null
    val liveUserPref=MutableLiveData<UserPref>()


    fun makeAnnounceRequest(requestData: RequestData) {
        val client = OkHttpClient.Builder().build()
        responseState.value = ResponseState.Loading
        val request = Request.Builder().url(requestData.url).build()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val list = parseHtmlForTitles(response, requestData)
                    liveResponseData.postValue(list)
                    responseState.value = ResponseState.Success
                } else {
                    responseState.value = ResponseState.Fail
                }
            }catch (e:Exception){
                responseState.value=ResponseState.Fail
            }
        }
    }

    private fun parseHtmlForTitles(response: Response, requestData: RequestData): MutableList<Announce> {
        val list = mutableListOf<Announce>()
        val doc = Jsoup.parse(response.body?.string())
        val select0 = requestData.select0
        val select1 = requestData.select1
        var url = response.request.url.toString()
        println(url)
        if(url=="http://guzelsanat.erciyes.edu.tr/Duyurular"){
            url="http://guzelsanat.erciyes.edu.tr/"
        }
        if(url=="https://www.erciyes.edu.tr/Tum-Duyurular/0"){
            url="https://www.erciyes.edu.tr"
        }

        if (url=="https://www.erciyes.edu.tr/Tum-Duyurular/-3"){
            url="https://www.erciyes.edu.tr"
        }
        if (url=="http://egitim.erciyes.edu.tr/announcement/1/1/"){
            url="http://egitim.erciyes.edu.tr"
        }
        repeat(25) { index ->
            val cssSelector = select0 + index + select1
            val elements = doc.select(cssSelector)
            if (elements.isEmpty()) return@repeat
            else {
                val title = elements.text()
                var indexUrl = url + (elements.select("a").attr("href"))
                if (!indexUrl.startsWith("http",true)){
                    indexUrl=url+indexUrl
                }
                val announce = Announce(title, indexUrl)
                list.add(announce)
            }
        }
        return list
    }
}