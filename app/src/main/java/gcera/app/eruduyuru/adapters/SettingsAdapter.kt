package gcera.app.eruduyuru.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import gcera.app.eruduyuru.R
import gcera.app.eruduyuru.models.SettingItem
import gcera.app.eruduyuru.models.SettingItemType
import kotlinx.android.synthetic.main.setting_spinner_type.view.*
import kotlinx.android.synthetic.main.setting_switch_type.view.*

private const val switch_type = 1
private const val spinner_type = 2

class SettingsAdapter(private val list: List<SettingItem>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var announceTimeSpinnerListener: (Int) -> Unit = {}
    private var mealTimeSpinnerListener: (Int) -> Unit = {}
    private var themeSwitchListener: (Boolean) -> Unit = {}
    private var announceSwitchListener: (Boolean) -> Unit = {}
    private var mealSwitchListener: (Boolean) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            switch_type -> {
                SwitchTypeViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.setting_switch_type, parent, false)
                )
            }
            else -> {
                SpinnerTypeViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.setting_spinner_type, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val settingItem = list[position]
        when (holder) {
            is SwitchTypeViewHolder -> {
                val settingData = settingItem.type as SettingItemType.Switch
                with(holder.view) {
                    switchSettingTitle.text = settingData.title
                    switchSettingContent.text = settingData.content
                    settingSwitch.isChecked = settingData.checked
                    val listener= when (settingData.title) {
                        "Yemekhane Bildirimi" -> mealSwitchListener
                        "Duyuru Bildirimi" -> announceSwitchListener
                        else -> themeSwitchListener
                    }
                    settingSwitch.setOnCheckedChangeListener { _, isChecked ->
                       listener(isChecked)
                    }
                }
            }
            is SpinnerTypeViewHolder -> {
                val settingData = settingItem.type as SettingItemType.Spinner
                with(holder.view) {
                    spinnerSettingTitle.text = settingData.title
                    spinnerSettingContent.text = settingData.content
                    ArrayAdapter.createFromResource(
                        context,
                        settingData.array,
                        R.layout.spinner_text
                    ).also {
                        settingSpinner.adapter = it
                    }
                    settingSpinner.setSelection(settingData.selectedItem)
                    if (settingData.disable) {
                        settingSpinner.isEnabled = false
                    }
                    settingSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                pos: Int,
                                id: Long
                            ) {
                                if (settingData.array == R.array.announce_control_time_options) {
                                    announceTimeSpinnerListener(pos)
                                } else {
                                    mealTimeSpinnerListener(pos)
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position].type) {
            is SettingItemType.Switch -> switch_type
            else -> spinner_type
        }
    }

    fun setAnnounceTimeSpinnerListener(listener: (Int) -> Unit) {
        this.announceTimeSpinnerListener = listener
    }

    fun setMealTimeSpinnerListener(listener: (Int) -> Unit) {
        this.mealTimeSpinnerListener = listener
    }

    fun setAnnounceSwitchListener(listener:(Boolean)->Unit){
        this.announceSwitchListener=listener
    }

    fun setMealSwitchListener(listener:(Boolean)->Unit){
        this.mealSwitchListener=listener
    }

    fun setThemeSwitchListener(listener:(Boolean)->Unit){
        this.themeSwitchListener=listener
    }

    class SwitchTypeViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    class SpinnerTypeViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}