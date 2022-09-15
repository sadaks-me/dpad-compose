package dev.berggren.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.hilt.navigation.compose.hiltViewModel
import dev.berggren.DashboardBackground
import dev.berggren.ui.menu.MenuViewModel
import dev.berggren.ui.navigation.Screen


@Composable
fun EmptyPage() {
    val itemsPerRow = 5

    val sections = listOf(
        "Recommended",
        "Trainers",
    )

    val sectionMap = sections.map { section ->
        (0..itemsPerRow).map { rowIndex -> "$section-$rowIndex" }
    }

    val viewModel = hiltViewModel<MenuViewModel>()
    Box(
        Modifier
            .fillMaxSize()
            .background(DashboardBackground)
            .onFocusChanged {
                if (it.hasFocus) viewModel.menuManager.close()
            },
        contentAlignment = Alignment.Center
    ) {
        Rails(items = sectionMap) {
            viewModel.navigator.push(Screen.Empty.route)
        }
    }
}

