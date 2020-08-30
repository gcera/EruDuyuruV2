package gcera.app.eruduyuru.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.adapters.SettingsAdapter
import gcera.app.eruduyuru.models.SettingItem
import gcera.app.eruduyuru.models.SettingItemType
import gcera.app.eruduyuru.models.UserPref
import gcera.app.eruduyuru.objects.AppData
import gcera.app.eruduyuru.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragments_setting.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsFragment:Fragment(R.layout.fragments_setting) {
    private lateinit var adapter:SettingsAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userPref: UserPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel=ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        userPref=mainViewModel.liveUserPref.value?:AppData.defaultUserPref
        mainViewModel.liveUserPref.observe(viewLifecycleOwner, {
            userPref=it
        })
        adapter= SettingsAdapter(list(userPref),requireContext())
        setupAdapterListeners()
        settingsRV.adapter=adapter
    }

    private fun setupAdapterListeners(){
        adapter.setAnnounceTimeSpinnerListener {pos->
            userPref.announceTime=pos
            mainViewModel.liveUserPref.postValue(userPref)
        }
        adapter.setMealTimeSpinnerListener { pos->
            userPref.mealTime=pos
            mainViewModel.liveUserPref.postValue(userPref)
        }
        adapter.setAnnounceSwitchListener {isChecked->
            userPref.isAnnounceActive=isChecked
            mainViewModel.liveUserPref.postValue(userPref)
        }
        adapter.setMealSwitchListener {isChecked->
            userPref.isMealActive=isChecked
            mainViewModel.liveUserPref.postValue(userPref)
        }
        adapter.setThemeSwitchListener {isChecked->
            userPref.isDarkThemeActive=isChecked
            mainViewModel.liveUserPref.postValue(userPref)
        }
    }

    private fun list(userPref: UserPref)= listOf(
        SettingItem(
            type = SettingItemType.Switch(
                title = "Tema",
                content = "Koyu tema etkinleştir",
                checked = userPref.isDarkThemeActive
            )
        ),
        SettingItem(
            type = SettingItemType.Switch(
                title = "Duyuru Bildirimi",
                content = "Yeni duyuru olduğunda bana bildir",
                checked = userPref.isAnnounceActive
            )
        ),
        SettingItem(
            type = SettingItemType.Spinner(
                title = "Duyuru Kontrol Sıklığı",
                content = "Duyuru Kontrol Sıklığı",
                selectedItem = userPref.announceTime,
                array = R.array.announce_control_time_options,
                disable = !userPref.isAnnounceActive
            )
        ),
        SettingItem(
            type = SettingItemType.Switch(
                title = "Yemekhane Bildirimi",
                content = "Günlük yemek listesini bana bildir",
                checked = userPref.isMealActive
            )
        ),
        SettingItem(
            type = SettingItemType.Spinner(
                title = "Yemek Zamanı",
                content = "Yemekhane bildirim zamanı",
                selectedItem = userPref.mealTime,
                array = R.array.meal_control_time_options,
                disable = !userPref.isMealActive
            )
        )
    )
}