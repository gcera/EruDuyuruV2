package gcera.app.eruduyuru.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.adapters.DetailAdapter
import gcera.app.eruduyuru.models.AnnounceDetail
import gcera.app.eruduyuru.models.RequestData
import gcera.app.eruduyuru.objects.materialAlertDialog
import gcera.app.eruduyuru.objects.negativeButton
import gcera.app.eruduyuru.objects.positiveButton
import gcera.app.eruduyuru.ui.viewmodels.DetailViewModel
import gcera.app.eruduyuru.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.announce_item.*
import kotlinx.android.synthetic.main.fragments_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import m7mdra.com.htmlrecycler.HtmlRecycler
import m7mdra.com.htmlrecycler.source.StringSource

@ExperimentalCoroutinesApi
class DetailFragment:Fragment(R.layout.fragments_detail) {
    private lateinit var mainLink:String
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var mainViewModel: MainViewModel
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel=ViewModelProviders.of(requireActivity()).get(DetailViewModel::class.java)
        mainViewModel=ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        mainLink= mainViewModel.liveRequestData.value?.url.toString()
        val detailRequestData= mainViewModel.liveRequestData.value?.indexSelect?.let {
            RequestData(mainViewModel.indexUrl,mainViewModel.indexSelect,imgId = 0)
        }
        if (detailRequestData != null && isNetworkAvailable(requireContext()) ) {
            detailViewModel.makeDetailRequest(
                mainUrl = mainLink,
                requestData = detailRequestData,
                title = mainViewModel.title
            )
        }
        lifecycleScope.launch {
            detailViewModel.detailState.collect {state->
                when(state){
                    is DetailViewModel.DetailState.Loading->{}

                    is DetailViewModel.DetailState.Fail->{showGoBrowserDialog()}

                    is DetailViewModel.DetailState.Success->{ onSuccess(state.announceDetail) }
                }
            }
        }
        detailFailTV.setOnClickListener {
            showGoBrowserDialog()
        }
    }

    private fun onSuccess(announceDetail: AnnounceDetail){
        detailTitleText.text = announceDetail.title
        detailFailTV.visibility=View.VISIBLE
        if (announceDetail.detail.isEmpty()){
            showGoBrowserDialog()
        }else{
            val source= StringSource(announceDetail.detail)
            HtmlRecycler.Builder(requireContext())
                .setSource(source)
                .setRecyclerView(detailRV)
                .setAdapter(DetailAdapter()).build()
        }
    }

    private fun showGoBrowserDialog(){
        materialAlertDialog(requireContext()){
            setTitle("Tarayıcıya Yönlendiriliyorsunuz")
            setMessage("Anlaşılan içerik görüntülenirken bir sorun oluştu." +
                    "İçeriği varsayılan tarayıcızda " +
                    "açmak ister misiniz?")
            positiveButton("Evet"){dialog, item ->
                Intent(Intent.ACTION_VIEW).let {
                    it.data = Uri.parse(mainViewModel.indexUrl)
                    startActivity(it)
                }
            }
            negativeButton("Hayır"){dialog, item ->
                dialog.dismiss()
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetwork
        return activeNetworkInfo != null
    }
}