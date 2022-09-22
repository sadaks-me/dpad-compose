package dev.berggren.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import dev.berggren.ui.EmptyPage
import dev.berggren.ui.Home

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BuildNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    AnimatedNavHost(
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
        composable(
            HomeScreen.Dash.route,
            enterTransition = {
                when (initialState.destination.route) {
                    EmptyScreen.Page.route ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            exitTransition = {
                null
            },
            popEnterTransition = {
                null
            },
            popExitTransition = {
                null
            }
        ) {
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
        composable(
            EmptyScreen.Page.route,
            enterTransition = {
                when (initialState.destination.route) {
                    HomeScreen.Dash.route ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    HomeScreen.Dash.route ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    HomeScreen.Dash.route ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    HomeScreen.Dash.route ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            }
        ) {
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