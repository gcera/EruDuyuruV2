package gcera.app.eruduyuru.ui.fragments

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.adapters.DepartmentsAdapter
import gcera.app.eruduyuru.models.DepartsListItem
import gcera.app.eruduyuru.models.RequestData
import gcera.app.eruduyuru.objects.AppData
import gcera.app.eruduyuru.objects.Constants
import gcera.app.eruduyuru.ui.viewmodels.DepartmentsViewModel
import gcera.app.eruduyuru.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragments_departs.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class DepartmentsFragment : Fragment(R.layout.fragments_departs) {
    private val viewModel: DepartmentsViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: DepartmentsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var userDataIndex=-1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences=requireContext().getSharedPreferences(Constants.SHARED_PREF_NAME,Context.MODE_PRIVATE)
        userDataIndex=sharedPreferences.getInt(Constants.KEY_DATA_INDEX,-1)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        initAdapter()
        departsRV.adapter = adapter
        lifecycleScope.launch {
            viewModel.departState.collect {
                setAdapterData(it)
            }
        }

    }

    private suspend fun setAdapterData(data: List<DepartsListItem>) {
        withContext(Dispatchers.Main) {
            adapter.data = data
            adapter.notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        adapter = DepartmentsAdapter(
            viewModel.departState.value,
            clickAction = { pos ->
                when (pos) {
                    0 -> {
                        val forMuh =
                            (viewModel.intentFlow.value === DepartmentsViewModel.Intent.Muhendis)
                        if (!forMuh) {
                            seeMuh()
                        } else {
                            if (userDataIndex==-1){
                                showChangeDefaultDepartDialog(pos,forMuh)
                            }
                            val requestData = AppData.getRequestData(pos, true)
                            mainViewModel.liveRequestData.value = requestData
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.announceListFragment)
                        }
                    }
                    in 1..14 -> {
                        val forMuh =
                            (viewModel.intentFlow.value === DepartmentsViewModel.Intent.Muhendis)
                        if (userDataIndex==-1){
                            showChangeDefaultDepartDialog(pos,forMuh)
                        }else{
                            val requestData = AppData.getRequestData(pos, forMuh)
                            mainViewModel.liveRequestData.postValue(requestData)
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.announceListFragment)
                        }
                    }
                }
            },
            onLongPress = { pos ->
                val forMuh = (viewModel.intentFlow.value === DepartmentsViewModel.Intent.Muhendis)
                if (forMuh) {
                    showChangeDefaultDepartDialog(pos, forMuh)
                } else if (pos != 0) {
                    showChangeDefaultDepartDialog(pos, forMuh)
                }
                true
            }
        )
    }

    private fun seeMuh() {
        viewModel.intentFlow.value = DepartmentsViewModel.Intent.Muhendis
    }
    private fun showChangeDefaultDepartDialog(pos: Int, forMuh: Boolean) {
        val data=AppData.getRequestData(pos,forMuh )
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Erü Duyuru")
            .setMessage("${data.departName} varsayılan bölüm olarak kaydedilsinmi?\n Daha sonra değiştirmek istersen bölüm kartı üzerine basılı tutman yeterli." )
            .setPositiveButton("Evet") { p0, p1 ->
                requireContext().getSharedPreferences(
                    Constants.SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                ).edit().apply {
                    putInt(Constants.KEY_DATA_INDEX, pos)
                    putBoolean(Constants.KEY_FOR_MUH, forMuh)
                }.apply()
                mainViewModel.liveRequestData.value= data
                Toast.makeText(requireContext(),"${data.departName} kaydedildi...",Toast.LENGTH_SHORT).show()
                findNavController().run {
                    popBackStack()
                    navigate(R.id.announceListFragment)
                }
            }
            .setNegativeButton("Hayır"){p0,p1->
                p0.dismiss()
            }.create().show()
    }
}