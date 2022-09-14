package dev.berggren.ui.navigation

import androidx.navigation.NavOptionsBuilder

sealed class NavigatorEvent {
    object Pop : NavigatorEvent()
    class Push(
        val destination: String,
        val builder: NavOptionsBuilder.() -> Unit
    ) : NavigatorEvent()
    class PopDeepLink(
        val destination: String,
        val builder: NavOptionsBuilder.() -> Unit
    ) : NavigatorEvent()
    object Clear : NavigatorEvent()
}
