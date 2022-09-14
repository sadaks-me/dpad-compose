package dev.berggren.ui.menu

import dev.berggren.util.coroutines.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
internal class MenuManagerImpl @Inject constructor(
//    val focusPageController: FocusPageController,
    coroutineContextProvider: CoroutineContextProvider
) : MenuManager, CoroutineScope {

    private val menuEvents = Channel<MenuManagerEvent>()
    override val events = menuEvents.receiveAsFlow()

    override var isOpen: Boolean = false

    override fun open(): Boolean {
//        focusPageController.isEnabled = false
        isOpen = true
        val res = menuEvents.trySend(MenuManagerEvent.Open).isSuccess
        launch {
            delay(700)
//            focusPageController.isEnabled = true
        }
        return res
    }
    override fun close(): Boolean {
//        focusPageController.isEnabled = false
        isOpen = false
        val res = menuEvents.trySend(MenuManagerEvent.Close).isSuccess
        launch {
            delay(700)
//            focusPageController.isEnabled = true
        }
        return res
    }
    override fun hide(): Boolean {
//        focusPageController.isEnabled = false
        isOpen = false
        val res = menuEvents.trySend(MenuManagerEvent.Hide).isSuccess
        launch {
            delay(700)
//            focusPageController.isEnabled = true
        }
        return res
    }

    override val coroutineContext: CoroutineContext = coroutineContextProvider.io
}
