package gcera.app.eruduyuru.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.adapters.DepartmentsAdapter
import gcera.app.eruduyuru.models.DepartsListItem
import gcera.app.eruduyuru.models.RequestData
import gcera.app.eruduyuru.objects.*
import gcera.app.eruduyuru.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragments_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

@ExperimentalCoroutinesApi
class MainFragment : Fragment(R.layout.fragments_main) {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainDepartmentAnnounceText.text = mainViewModel.mainAnnounceText
        mainEruAnnounceText.text = mainViewModel.mainEruAnnounceText
        mainEruNewsText.text = mainViewModel.mainEruNewsText
        val forMuh = sharedPreferences.getBoolean(Constants.KEY_FOR_MUH, false)
        val index = sharedPreferences.getInt(Constants.KEY_DATA_INDEX, -1)
        val requestData = if (index != -1) {
            AppData.getRequestData(index, forMuh)
        } else { null }
        runInFirstRun(requestData)
        val departName = requestData?.departName ?: "Varsayılan Bölüm"
        mainMenuRV.setHasFixedSize(true)
        val id = when {
            forMuh and (index != -1) -> AppData.MUH_LOGOLAR[index]
            !forMuh and (index != -1) -> AppData.FAKULTE_LOGOLAR[index]
            else -> R.drawable.ic_main
        }
        DepartmentsAdapter(
            list(departName, id),
            { pos ->
                when (pos) {
                    0 -> { clickDefault(requestData) }
                    1 -> { clickEruAnnounce() }
                    2 -> { clickEruNews() }
                    3 -> { clickObisisAnnounce() }
                }
            },
            { true }
        ).also { mainMenuRV.adapter = it }
        mainMealTitle.setOnClickListener {
            if (mainMealTextLayout.visibility == View.GONE) {
                mainMealTextLayout.visibility = View.VISIBLE
                mainMealTitleTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_expand_less,0)
                if (mainViewModel.mainMealText == "" && isNetworkAvailable(requireContext())) {
                    mainMealTextLayout.showSkeleton()
                    lifecycleScope.launch(Dispatchers.IO){
                        val data = AppData.getMealData()
                        val client = OkHttpClient.Builder().build()
                        val request = Request.Builder().url(data.url).build()
                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            val cssSelector = data.select0
                            val elements = Jsoup.parse(response.body?.string()).select(cssSelector)
                            val mealText = HtmlCompat.fromHtml(elements.html(),0)
                            withContext(Dispatchers.Main) {
                                if (mainEruAnnounceText != null) {
                                    mainMealTextLayout.showOriginal()
                                    mainMealText.text = mealText
                                    if (requestData != null) {
                                        mainViewModel.indexUrl=requestData.url
                                    }
                                }
                                mainViewModel.mainMealText = mealText.toString()
                            }
                        }
                    }
                } else {
                    mainMealText.text = mainViewModel.mainMealText
                }
            } else {
                mainMealTextLayout.visibility = View.GONE
                mainMealTitleTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_expand_more,0)
            }
        }
    }

    private fun list(departName: String, imgId: Int) = listOf(
        DepartsListItem(name = departName, resId = imgId),
        DepartsListItem(name = "Erciyes Üni. Duyurular", resId = R.drawable.ic_main_announcement),
        DepartsListItem(name = "Erciyes Üni. Haberler", resId = R.drawable.ic_news),
        DepartsListItem(name = "Obisis Duyurular", resId = R.drawable.ic_obisis)
    )

    private fun clickDefault(requestData: RequestData?) {
        if (requestData != null) {
            mainViewModel.liveRequestData.value = requestData
            navigateAnnounceListFragment()
        } else {
            showSelectDepartDialog()
        }
    }

    private fun showSelectDepartDialog() {
        materialAlertDialog(requireContext()) {
            setTitle("Erü Duyuru")
            setMessage("Henüz bölüm seçilmemiş, şimdi seçmek ister misiniz?")
            positiveButton("Evet") { _, _ ->
                findNavController().run {
                    popBackStack()
                    navigate(R.id.departmentsFragment)
                }
            }
            negativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }
        }
    }

    private fun clickEruAnnounce() {
        val requestData = AppData.getEruAnnounceData()
        mainViewModel.liveRequestData.value = requestData
        navigateAnnounceListFragment()
    }

    private fun clickEruNews() {
        val requestData = AppData.getEruNewsData()
        mainViewModel.liveRequestData.value = requestData
        navigateAnnounceListFragment()
    }

    private fun clickObisisAnnounce() {
        val requestData = AppData.getObisisData()
        mainViewModel.liveRequestData.value = requestData
        navigateAnnounceListFragment()
    }
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetwork
        return activeNetworkInfo != null
    }
    private fun navigateAnnounceListFragment() {
        findNavController().run {
            navigate(R.id.announceListFragment)
        }
    }

    private fun runInFirstRun(requestData: RequestData?) {
        if (requestData != null && isNetworkAvailable(requireContext())) {
            lifecycleScope.launch(Dispatchers.IO) {
                if (mainViewModel.mainAnnounceText.isEmpty()) {
                    mainDepartmentAnnounceCard.showSkeleton()
                    launch {
                        val client = OkHttpClient.Builder().build()
                        val request = Request.Builder().url(requestData.url).build()
                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            val cssSelector = "${requestData.select0}1${requestData.select1}"
                            val elements = Jsoup.parse(response.body?.string()).select(cssSelector)
                            val title = elements.text()
                            withContext(Dispatchers.Main) {
                                if (mainDepartmentAnnounceText != null) {
                                    mainDepartmentAnnounceText.text = title
                                    mainDepartmentAnnounceCard.showOriginal()
                                }
                                mainViewModel.mainAnnounceText = title
                            }
                        }
                    }
                }

                if (mainViewModel.mainEruAnnounceText.isEmpty()) {
                    mainEruAnnounceCard.showSkeleton()
                    launch {
                        val data = AppData.getEruAnnounceData()
                        val client = OkHttpClient.Builder().build()
                        val request = Request.Builder().url(data.url).build()
                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            val cssSelector = "${data.select0}1${data.select1}"
                            val elements = Jsoup.parse(response.body?.string()).select(cssSelector)
                            val title = elements.text()
                            withContext(Dispatchers.Main) {
                                if (mainEruAnnounceText != null) {
                                    mainEruAnnounceText.text = title
                                    mainEruAnnounceCard.showOriginal()
                                }
                                mainViewModel.mainEruAnnounceText = title
                            }
                        }
                    }
                }

                if (mainViewModel.mainEruNewsText.isEmpty()) {
                    mainEruNewsCard.showSkeleton()
                    launch {
                        val data = AppData.getEruNewsData()
                        val client = OkHttpClient.Builder().build()
                        val request = Request.Builder().url(data.url).build()
                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            val cssSelector = "${data.select0}1${data.select1}"
                            val elements = Jsoup.parse(response.body?.string()).select(cssSelector)
                            val title = elements.text()
                            withContext(Dispatchers.Main) {
                                if (mainEruNewsText != null) {
                                    mainEruNewsText.text = title
                                    mainEruNewsCard.showOriginal()
                                }
                                mainViewModel.mainEruNewsText = title
                            }
                        }
                    }
                }
            }
        }
    }
}
