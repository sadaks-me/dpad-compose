package dev.berggren.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dev.berggren.DashboardBackground
import dev.berggren.ui.menu.Menu
import dev.berggren.ui.menu.MenuManager
import dev.berggren.ui.menu.MenuManagerEvent
import dev.berggren.ui.menu.MenuStateEnum
import dev.berggren.ui.navigation.BuildNavGraph
import dev.berggren.ui.navigation.Navigator
import dev.berggren.ui.navigation.NavigatorEvent

lateinit var LocalNavController: ProvidableCompositionLocal<NavHostController>

object LocalNavHostController {
    @Composable
    fun Init() {
        with(LocalContext.current) {
            LocalNavController = compositionLocalOf {
                NavHostController(this)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(
    navigator: Navigator,
    menuManager: MenuManager,
) {
    val navController = rememberAnimatedNavController()
    val viewModel = hiltViewModel<AppViewModel>()
    val selectedMenu = viewModel.selectedMenu.observeAsState(null)
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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
    val animatedWidth by animateDpAsState(
        targetValue = when (menuState.value!!) {
            MenuStateEnum.HIDDEN -> 0.dp
            MenuStateEnum.CLOSED -> 50.dp
            MenuStateEnum.OPEN -> 143.dp
        },
        animationSpec = tween(250, easing = LinearEasing)
    )
    CompositionLocalProvider(LocalNavController provides navController,
        content = {
            BackHandler(
                enabled = navController.currentDestination?.route?.contains("player") == false,
            ) {
                navController.currentDestination?.route?.let {
                    viewModel.checkBackHandler(context, it, focusManager)
                }
            }
            Box(modifier = Modifier.background(DashboardBackground)) {
                Menu(selectedMenu.value, menuState.value!!)
                Scaffold(
                    modifier = Modifier.padding(start = animatedWidth),
                    backgroundColor = DashboardBackground,
                ) {
                    BuildNavGraph(navController, it)
                }
            }
        })
}