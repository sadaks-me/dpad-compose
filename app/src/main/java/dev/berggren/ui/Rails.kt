package dev.berggren.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.berggren.*
import dev.berggren.ui.menu.MenuViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> Rails(
    items: List<List<T>>,
    onItemSelected: (() -> Unit)? = null,
) {
    val viewModel = hiltViewModel<MenuViewModel>()
    var focussedX: Int? by rememberSaveable { mutableStateOf(null) }
    var focussedY: Int? by rememberSaveable { mutableStateOf(null) }

    Box(
        Modifier
            .fillMaxSize()
            .onFocusChanged {
                if (it.hasFocus) viewModel.menuManager.close()
            }
            .background(DashboardBackground)
    ) {
        HeroBackground("selectedItem")
        Hero("selectedItem")
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 320.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollableGrid(
                items = items
            ) { item, position ->
                val elementPaddingAndHalfOfNextBox = with(LocalDensity.current) {
                    (boxPadding + (boxSize).div(4)).toPx()
                }
                Focusable(
                    isDefault = position.rowIndex == focussedX && position.columnIndex == focussedY,
                    onClick = {
                        onItemSelected?.invoke()
                    },
                    scrollPadding = Rect(
                        left = elementPaddingAndHalfOfNextBox,
                        top = elementPaddingAndHalfOfNextBox,
                        right = elementPaddingAndHalfOfNextBox,
                        bottom = elementPaddingAndHalfOfNextBox
                    ),
                    onFocusChanged = {
                        if (it) {
                            focussedX = position.rowIndex
                            focussedY = position.columnIndex
                        }
                        println("Selected index: position.rowIndex: ${position.rowIndex} - position.columnIndex: ${position.columnIndex}")
                    }
                ) {
                    ColoredBox(
                        color = transparent
                    )
                }
            }
        }
    }
}

@Composable
fun HeroBackground(selectedItem: String) {
    Box(
        Modifier
            .height(320.dp)
            .fillMaxWidth()
            .animateContentSize(tween(500, easing = LinearEasing)),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Hero(
    selectedItem: String,
    onFocusChanged: ((isFocussed: Boolean) -> Unit)? = null
) {
    var isFocussed by remember { mutableStateOf(true) }
    Box(
        Modifier
            .height(320.dp)
            .padding(bottom = 24.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .onFocusChanged {
                isFocussed = it.isFocused
                onFocusChanged?.invoke(isFocussed)
            }
            .focusProperties { up = FocusRequester.Cancel },
        contentAlignment = Alignment.BottomStart
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(1f)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            darkGradientColor,
                        ),
                    )
                )
        )
        Row(
            Modifier
                .padding(bottom = 30.dp, start = 20.dp)
                .alpha(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            Focusable(
                isDefault = true,
                borderWidth = 0.dp,
                magnify = false,
                onFocusChanged = {
                    isFocussed = it
                }
            ) {
                ColoredBox(
                    height = 40.dp,
                    width = 200.dp,
                    color = if (it) highlightColor else primaryColor20
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Focusable(
                borderWidth = 0.dp,
                magnify = false,
                onFocusChanged = {
                    isFocussed = it
                }
            ) {
                ColoredBox(
                    height = 40.dp,
                    width = 200.dp,
                    color = if (it) highlightColor else primaryColor20
                )
            }
        }
    }
}