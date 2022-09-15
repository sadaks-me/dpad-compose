package dev.berggren.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import dev.berggren.ui.menu.MenuViewModel
import dev.berggren.ui.navigation.Screen

@Composable
fun Home() {
    val itemsPerRow = 10

    val sections = listOf(
        "Recommended",
        "Trainers",
        "Workouts",
        "Practices",
        "Programmes",
    )

    val sectionMap = sections.map { section ->
        (0..itemsPerRow).map { rowIndex -> "$section-$rowIndex" }
    }
    val viewModel = hiltViewModel<MenuViewModel>()
    Rails(
        sectionMap
    ) { viewModel.navigator.push(Screen.Empty.route) }

}

