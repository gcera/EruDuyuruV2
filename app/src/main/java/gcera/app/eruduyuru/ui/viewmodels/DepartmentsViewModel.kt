package gcera.app.eruduyuru.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gcera.app.eruduyuru.models.DepartsListItem
import gcera.app.eruduyuru.objects.AppData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
class DepartmentsViewModel : ViewModel() {

    private val fakulteList = ArrayList<DepartsListItem>().apply {
        for (i in 0..14) {
            add(DepartsListItem(AppData.FAKULTELER[i], AppData.FAKULTE_LOGOLAR[i]))
        }
    }

    private val muhList = ArrayList<DepartsListItem>().apply {
        for (i in 0..13) {
            add(DepartsListItem(AppData.MUH_BOLUMLER[i], AppData.MUH_LOGOLAR[i]))
        }
    }

    sealed class Intent{
        object Fakulte:Intent()
        object Muhendis:Intent()
    }

    val intentFlow= MutableStateFlow<Intent>(Intent.Fakulte)
    val departState = MutableStateFlow(fakulteList)

    init {
        viewModelScope.launch {
            intentFlow.collect {
                when(it){
                    Intent.Fakulte->{departState.value=fakulteList}
                    Intent.Muhendis->{departState.value=muhList}
                }
            }
        }
    }
}