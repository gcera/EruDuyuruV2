package gcera.app.eruduyuru.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.models.UserPref
import gcera.app.eruduyuru.objects.*
import gcera.app.eruduyuru.objects.Constants.KEY_DATA_INDEX
import gcera.app.eruduyuru.objects.Constants.KEY_FOR_MUH
import gcera.app.eruduyuru.objects.Constants.SHARED_PREF_NAME
import gcera.app.eruduyuru.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private var isDarkTheme = false
    private lateinit var viewModel: MainViewModel
    private lateinit var userPref: UserPref
    override fun onCreate(savedInstanceState: Bundle?) {
        preSetup()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("after setcontent")
        setupNavigation()
        setupUserRequestData()
        navigateDetailFragment(intent)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDestroy() {
        super.onDestroy()
        writeUserPref()
        setAlarms()
    }

    private fun setupNavigation() {
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.visibility = when (destination.id) {
                R.id.detailFragment -> View.GONE
                else -> View.VISIBLE
            }
        }
        bottomNavigationView.setOnNavigationItemReselectedListener { }
    }

    private fun setupUserRequestData() {
        val sharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val index = sharedPref.getInt(KEY_DATA_INDEX, -1)
        println(index)
        val forMuh = sharedPref.getBoolean(KEY_FOR_MUH, false)
        if (index < 0 && !forMuh) {
            showChooseDepartDialog()
        } else {
            AppData.getRequestData(index, forMuh).also {
                viewModel.liveRequestData.value = it
            }
        }
    }

    private fun readUserPref() {
        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val userPrefString = sharedPreferences.getString(Constants.KEY_USER_PREF, "") ?: ""
        if (userPrefString.isNotEmpty()) {
            val userPreference = Gson().fromJson(userPrefString, UserPref::class.java)
            userPref = userPreference
            viewModel.liveUserPref.value = userPreference
        } else {
            viewModel.liveUserPref.value = AppData.defaultUserPref
        }
    }

    private fun writeUserPref() {
        val userPrefString = Gson().toJson(userPref)
        println(userPrefString)
        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        sharedPreferences.edit().putString(Constants.KEY_USER_PREF, userPrefString).apply()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateDetailFragment(intent)
    }

    private fun navigateDetailFragment(intent: Intent?) {
        if (intent?.action == Constants.ACTION_NAVIGATE_DETAIL) {
            val title = intent.getStringExtra(Constants.KEY_NOTIFY_TITLE)
            if (title != null) {
                viewModel.title = title
            }
            val indexUrl = intent.getStringExtra(Constants.KEY_NOTIFY_INDEXURL)
            if (indexUrl != null) {
                viewModel.indexUrl = indexUrl
            }
            val indexSelect = intent.getStringExtra(Constants.KEY_NOTIFY_INDEX_SELECT)
            if (indexSelect != null) {
                viewModel.indexSelect = indexSelect
            }
            navHostFragment.findNavController().navigate(R.id.detailFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setAlarms() {
        Alarm.setCheckMealAlarm(this, userPref.isMealActive, userPref.mealTime)
        Alarm.setCheckLastAnnounceAlarmWithWorkManager(this, userPref.isAnnounceActive, userPref.announceTime)
    }

    private fun showChooseDepartDialog() {
        materialAlertDialog(this) {
            setTitle("Erü Duyuru")
            setMessage("Henüz bölüm seçilmemiş, şimdi seçmek ister misiniz?")
            positiveButton("Evet") { _, _ ->
                navHostFragment.findNavController().run {
                    popBackStack()
                    navigate(R.id.departmentsFragment)
                }
            }
            negativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }
        }
    }

    private fun preSetup() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        userPref = AppData.defaultUserPref
        readUserPref()
        if (userPref.isDarkThemeActive) {
            isDarkTheme = true
            setTheme(R.style.DarkTheme)
        }
        viewModel.liveUserPref.observe(this, {
            userPref = it
            println(userPref.isDarkThemeActive)
            if (isDarkTheme != userPref.isDarkThemeActive) {
                isDarkTheme = userPref.isDarkThemeActive
                recreate()
            }
        })
    }
}