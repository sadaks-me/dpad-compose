package dev.berggren.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berggren.ui.navigation.Navigator
import dev.berggren.util.coroutines.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MenuViewModel @Inject constructor(
    val menuManager: MenuManager,
    val navigator: Navigator,
    coroutineContextProvider: CoroutineContextProvider
) : ViewModel(), MenuManager by menuManager, Navigator by navigator, CoroutineScope {

    override val coroutineContext: CoroutineContext = coroutineContextProvider.io

    private val _selectedMenuOption = MutableLiveData(MenuOptionEnum.HOME)
    val selectedMenuOption = _selectedMenuOption as LiveData<MenuOptionEnum>

    private var shouldSetMenuItem: Boolean = true
    fun selectMenuOption(menuItemModel: MenuItemModel, replace: Boolean = true) {
        _selectedMenuOption.value = menuItemModel.menuOptionEnum
        println("pushing route: ${menuItemModel.route}")
        navigator.push(menuItemModel.route) {
            if (replace) {
                this.popUpTo("/") {
                    this.inclusive = true
                }
            }
        }
        menuManager.close()
    }

    fun setSelectedMenu(selected: MenuOptionEnum?) {
        if (selected != null && shouldSetMenuItem) {
            _selectedMenuOption.postValue(selected)
            shouldSetMenuItem = false
        }
    }
}
