package gcera.app.eruduyuru.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.adapters.AnnounceAdapter
import gcera.app.eruduyuru.models.Announce
import gcera.app.eruduyuru.models.RequestData
import gcera.app.eruduyuru.objects.AppData
import gcera.app.eruduyuru.objects.Constants
import gcera.app.eruduyuru.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragments_announces.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class AnnounceListFragment : Fragment(R.layout.fragments_announces) {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var requestData: RequestData
    private lateinit var dataList: List<Announce>
    private lateinit var adapter: AnnounceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        requestData = mainViewModel.liveRequestData.value!!
        dataList = mainViewModel.liveResponseData.value!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        announceListTitleIV.setImageResource(requestData.imgId)
        announceListTitleTV.text=requestData.departName
        mainViewModel.liveRequestData.observe(viewLifecycleOwner, {
            if ((mainViewModel.oldRequestData == null) or (mainViewModel.oldRequestData != it)) {
                requestData = it
                mainViewModel.oldRequestData = it
                if (isNetworkAvailable(requireContext()))
                    mainViewModel.makeAnnounceRequest(requestData)
            }
        })
        mainViewModel.liveResponseData.observe(viewLifecycleOwner, { dataList = it })
        adapter = AnnounceAdapter(dataList) { pos ->
            mainViewModel.title = dataList[pos].title
            mainViewModel.indexUrl = dataList[pos].indexUrl
            mainViewModel.indexSelect = requestData.indexSelect
            if (requestData.departName == "Obisis") {
                mainViewModel.indexSelect =
                    "${requestData.select0}${pos + 1}${AppData.obisisIndexSelect_}"
            }
            findNavController().navigate(R.id.detailFragment)
        }
        announcesRV.adapter = adapter
        lifecycleScope.launch {
            mainViewModel.responseState.collect {
                when (it) {
                    MainViewModel.ResponseState.Loading -> {
                        onLoading()
                    }
                    MainViewModel.ResponseState.Fail -> {
                        onFail()
                    }
                    MainViewModel.ResponseState.Success -> {
                        onSuccess()
                    }
                }
            }
        }
    }

    private suspend fun onLoading() = withContext(Dispatchers.Main) {
        announcesSkeleton.visibility = View.VISIBLE
        announcesSkeleton.showSkeleton()
    }

    private suspend fun onFail() = withContext(Dispatchers.Main) {
        announcesSkeleton.visibility = View.GONE
        Snackbar.make(
            requireView(),
            "Hata! Lütfen bağlantınızı kontrol edin!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private suspend fun onSuccess() = withContext(Dispatchers.Main) {
        val sharedPref =
            context?.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val defaultIndex = sharedPref?.getInt(Constants.KEY_DATA_INDEX, -1) ?: -1
        val forMuh = sharedPref?.getBoolean(Constants.KEY_FOR_MUH, false) ?: false
        if (defaultIndex >= 0) {
            if (requestData.departName == AppData.getRequestData(defaultIndex, forMuh).departName) {
                sharedPref?.edit()?.putString(Constants.KEY_LAST_ANNOUNCE, dataList[0].title)
            }
        }
        adapter.data = dataList
        announcesSkeleton.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetwork
        return activeNetworkInfo != null
    }
}