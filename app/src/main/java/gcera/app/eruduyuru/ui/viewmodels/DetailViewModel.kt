package gcera.app.eruduyuru.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gcera.app.eruduyuru.models.AnnounceDetail
import gcera.app.eruduyuru.models.RequestData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

@ExperimentalCoroutinesApi
class DetailViewModel:ViewModel() {

    sealed class DetailState{
        object Loading:DetailState()
        object Fail:DetailState()
        class Success(val announceDetail: AnnounceDetail):DetailState()
    }

    val detailState= MutableStateFlow<DetailState>(DetailState.Loading)

    fun makeDetailRequest(title:String,mainUrl:String,requestData: RequestData){
        var mUrl=mainUrl
        if(mUrl=="http://guzelsanat.erciyes.edu.tr/Duyurular"){
            mUrl="http://guzelsanat.erciyes.edu.tr/"
        }
        if(mUrl=="https://www.erciyes.edu.tr/Tum-Duyurular/0"){
            mUrl="https://www.erciyes.edu.tr"
        }

        if (mUrl=="https://www.erciyes.edu.tr/Tum-Duyurular/-3"){
            mUrl="https://www.erciyes.edu.tr"
        }
        val linkList= mutableListOf<String>()
        val client = OkHttpClient.Builder().build()
        detailState.value=DetailState.Loading
        val request= Request.Builder().url(requestData.url).build()
        viewModelScope.launch (Dispatchers.IO) {
            withTimeout(10*1000){
                try {
                    val response=client.newCall(request).execute()
                    if (response.isSuccessful){
                        val cssSelector=requestData.select0
                        val html=response.body?.string()
                        val doc= Jsoup.parse(html)
                        val linkElements=doc.select(cssSelector).select("a")
                        for (element in linkElements){
                            val url=element.attr("href")
                            if (url.isNotEmpty()){
                                if (url.startsWith("http", true)){
                                    linkList.add(url)
                                }
                                else {
                                    element.attr("href",mainUrl+url)
                                    linkList.add(mUrl + url)
                                }
                            }
                        }
                        val imgElements=doc.select(cssSelector).select("img")
                        for (img in imgElements){
                            val src=img.attr("src")
                            if (!src.startsWith("http",true))
                                img.attr("src",mUrl+src)
                        }
                        val detail=doc.select(cssSelector).html()
                        detailState.value=DetailState.Success(AnnounceDetail(title,detail,linkList))
                    }else{
                        detailState.value=DetailState.Fail
                    }
                }catch (e:Exception){
                    detailState.value=DetailState.Fail
                }
            }
        }
    }
}