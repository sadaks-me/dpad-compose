package dev.berggren.ui.app

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.berggren.ui.navigation.BuildNavGraph
import dev.berggren.ui.menu.Menu
import dev.berggren.ui.menu.MenuManager
import dev.berggren.ui.menu.MenuManagerEvent
import dev.berggren.ui.menu.MenuStateEnum
import dev.berggren.ui.navigation.Navigator
import dev.berggren.ui.navigation.NavigatorEvent

lateinit var LocalNavController: ProvidableCompositionLocal<NavHostController>

object LocalNavHostController {
    val current: NavHostController
        @Composable
        get() = LocalNavController.current

    @Composable
    fun Init() {
        with(LocalContext.current) {
            LocalNavController = compositionLocalOf {
                NavHostController(this)
            }
        }
    }
}

@Composable fun App(
    navigator: Navigator,
    menuManager: MenuManager,
) {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<AppViewModel>()
    val selectedMenu = viewModel.selectedMenu.observeAsState(null)

    LaunchedEffect(navController) {
        navigator.destinations.collect {
            when (val event = it) {
                is NavigatorEvent.Clear -> navController.popBackStack()
                is NavigatorEvent.Pop -> navController.navigateUp()
                is NavigatorEvent.Push -> navController.navigate(
                    event.destination,
                    event.builder
                )
                is NavigatorEvent.PopDeepLink -> {
                    navController.popBackStack()
                    navController.navigate(
                        event.destination,
                        event.builder
                    )
                }
            }
        }
    }

    LaunchedEffect(menuManager) {
        menuManager.events.collect {
            when (it) {
                is MenuManagerEvent.Open -> viewModel.setMenuState(MenuStateEnum.OPEN, true)
                is MenuManagerEvent.Close -> viewModel.setMenuState(MenuStateEnum.CLOSED, true)
                is MenuManagerEvent.Hide -> viewModel.setMenuState(MenuStateEnum.HIDDEN)
            }
        }
    }

    val menuState = viewModel.menuState.observeAsState()

    CompositionLocalProvider(LocalNavController provides navController,
        content = {
            Row() {
                Menu(selectedMenu.value, menuState.value!!)
                Scaffold {
                    BuildNavGraph(navController, it)
                }
            }
        })
}