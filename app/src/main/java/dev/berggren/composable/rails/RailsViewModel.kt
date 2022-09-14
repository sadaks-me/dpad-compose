package dev.berggren.composable.rails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.berggren.ui.menu.MenuManager
import dev.berggren.ui.navigation.Navigator
import dev.berggren.util.coroutines.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class RailsViewModel @Inject constructor(
    val navigator: Navigator, val menuManager: MenuManager,
    coroutineContextProvider: CoroutineContextProvider
) :
    ViewModel(), Navigator by navigator, MenuManager by menuManager, CoroutineScope {

    override val coroutineContext: CoroutineContext = coroutineContextProvider.io

    companion object {
        private const val TAG = "RailsViewModel"
    }

}