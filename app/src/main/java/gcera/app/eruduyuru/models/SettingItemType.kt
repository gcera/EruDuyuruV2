package gcera.app.eruduyuru.models

import androidx.annotation.ArrayRes

sealed class SettingItemType {
    data class Switch(
        val title:String,
        val content:String,
        val checked:Boolean=false
    ):SettingItemType()
    data class Spinner(
        val title:String,
        val content:String,
        val selectedItem:Int,
        @ArrayRes val array:Int,
        val disable:Boolean=false
    ):SettingItemType()
}