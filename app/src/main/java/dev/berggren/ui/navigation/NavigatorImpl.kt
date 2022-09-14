package dev.berggren.ui.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NavigatorImpl @Inject constructor() : Navigator {

    private val navigationEvents = Channel<NavigatorEvent>()
    override val destinations = navigationEvents.receiveAsFlow()
    override fun pop(): Boolean = navigationEvents.trySend(NavigatorEvent.Pop).isSuccess
    override fun push(
        route: String,
        builder: NavOptionsBuilder.() -> Unit,
    ): Boolean {
        return navigationEvents.trySend(NavigatorEvent.Push(route, builder)).isSuccess
    }
    override fun clear(): Boolean = navigationEvents.trySend(NavigatorEvent.Clear).isSuccess
    override fun popDeepLink(route: String, builder: NavOptionsBuilder.() -> Unit): Boolean {
        return navigationEvents.trySend(NavigatorEvent.PopDeepLink(route, builder)).isSuccess
    }
}
