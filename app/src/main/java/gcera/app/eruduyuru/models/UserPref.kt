package gcera.app.eruduyuru.models

data class UserPref (
    var isDarkThemeActive:Boolean,
    var isAnnounceActive:Boolean,
    var announceTime:Int,
    var isMealActive:Boolean,
    var mealTime:Int
)