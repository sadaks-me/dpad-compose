package dev.berggren.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.berggren.ui.EmptyPage
import dev.berggren.ui.Home
import dev.berggren.ui.menu.MenuStateEnum

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BuildNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navController,
        route = "/",
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        addHomeGraph()
        addEmptyGraph()
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
private fun NavGraphBuilder.addHomeGraph() {
    navigation(
        route = Screen.Home.route,
        startDestination = HomeScreen.Dash.route,
    ) {
        composable(HomeScreen.Dash.route) {
            Home()
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
private fun NavGraphBuilder.addEmptyGraph() {
    navigation(
        route = Screen.Empty.route,
        startDestination = EmptyScreen.Page.route,
    ) {
        composable(EmptyScreen.Page.route) {
            EmptyPage()
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Empty : Screen("empty")
}

sealed class HomeScreen(val route: String) {
    object Dash : HomeScreen("home/dash")
}

sealed class EmptyScreen(val route: String) {
    object Page : HomeScreen("empty/page")
}