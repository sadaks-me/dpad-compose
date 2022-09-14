package dev.berggren.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import dev.berggren.DashboardBackground
import dev.berggren.ui.menu.MenuViewModel

@Composable
fun EmptyPage() {
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
        Text(
            text = "Page: ${viewModel.selectedMenuOption.value!!.name}",
            style = MaterialTheme.typography.h2.copy(Color.White)
        )
    }
}