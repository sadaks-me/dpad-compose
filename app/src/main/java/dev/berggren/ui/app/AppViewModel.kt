package dev.berggren.ui.app

import android.app.Activity
import android.content.Context
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berggren.ui.menu.MenuManager
import dev.berggren.ui.menu.MenuOptionEnum
import dev.berggren.ui.menu.MenuStateEnum
import dev.berggren.ui.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val navigator: Navigator,
    private val menuManager: MenuManager,
) : ViewModel(),
    Navigator by navigator,
    MenuManager by menuManager {

    val selectedMenu = MutableLiveData<MenuOptionEnum>(null)
    private val _menuState = MutableLiveData(MenuStateEnum.HIDDEN)
    val menuState = _menuState as LiveData<MenuStateEnum>

    private val _lastActiveState = MutableLiveData(MenuStateEnum.CLOSED)
    private val lastActiveState = _lastActiveState as LiveData<MenuStateEnum>

    companion object {
        const val TAG = "AppViewModel"
    }

    fun setMenuState(state: MenuStateEnum, storeLastActive: Boolean = false) {
        if (storeLastActive && _menuState.value != MenuStateEnum.HIDDEN) {
            _lastActiveState.value = _menuState.value
        }

        if (state != _menuState.value) {
            _menuState.value = state
        }

        when (state) {
            MenuStateEnum.OPEN -> {
//                heroPlayer.pause()
            }
            MenuStateEnum.CLOSED -> {
//                heroPlayer.play()
            }
            MenuStateEnum.HIDDEN -> { }
        }
    }

    private val menuPages = listOf(
        "HomeScreen.Dash.route",
        "EmptyScreen.Page.route"
    )

    fun checkBackHandler(context: Context, route: String, focusManager: FocusManager) {
        when {
            menuPages.contains(route) -> {
                if (menuState.value == MenuStateEnum.CLOSED) {
                    menuManager.open()
                } else if (menuState.value == MenuStateEnum.OPEN) {
                    minimizeApp(context)
                }
            }
            else -> {
                pop()
            }
        }
    }

    private fun minimizeApp(context: Context) {
        (context as Activity).moveTaskToBack(false)
    }
}