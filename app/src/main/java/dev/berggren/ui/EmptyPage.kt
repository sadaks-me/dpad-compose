package dev.berggren.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import dev.berggren.ui.menu.MenuViewModel
import dev.berggren.ui.navigation.Screen
import dev.berggren.util.sectionMap

@Composable
fun EmptyPage() {
    val viewModel = hiltViewModel<MenuViewModel>()

    Rails(sectionMap.subList(0, 2)) { viewModel.navigator.push(Screen.Empty.route) }
}

