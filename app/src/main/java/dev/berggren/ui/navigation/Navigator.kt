package dev.berggren.ui.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface Navigator {
    fun pop(): Boolean
    fun push(
        route: String,
        builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true },
    ): Boolean
    fun clear(): Boolean
    fun popDeepLink(
        route: String,
        builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true },
    ): Boolean
    val destinations: Flow<NavigatorEvent>
}