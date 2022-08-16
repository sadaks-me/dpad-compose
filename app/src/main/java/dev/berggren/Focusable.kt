package dev.berggren

import android.view.KeyEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Focusable(
    onClick: (() -> Unit)? = null,
    onFocusChanged: ((isFocussed: Boolean) -> Unit)? = null,
    onLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    onUp: (() -> Unit)? = null,
    onDown: (() -> Unit)? = null,
    magnify: Boolean = true,
    borderWidth: Dp = 4.dp,
    unfocusedBorderColor: Color = Color(0x00f39c12),
    focusedBorderColor: Color = Color(0xfff39c12),
    content: @Composable (isFocused: Boolean) -> Unit,
) {
    val boxInteractionSource = remember { MutableInteractionSource() }
    val isItemFocused by boxInteractionSource.collectIsFocusedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (isItemFocused) 1f else (if (magnify) 0.85f else 1f)
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isItemFocused) focusedBorderColor else unfocusedBorderColor
    )
    var previousPress: PressInteraction.Press? by remember {
        mutableStateOf(null)
    }
    val scope = rememberCoroutineScope()

    var boxSize by remember {
        mutableStateOf(IntSize(0, 0))
    }

    LaunchedEffect(isItemFocused) {
        previousPress?.let {
            if (!isItemFocused) {
                boxInteractionSource.emit(
                    PressInteraction.Release(
                        press = it
                    )
                )
            }
        }
    }

    Box(
        Modifier
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .onGloballyPositioned {
                boxSize = it.size
            }
            .clickable(
                interactionSource = boxInteractionSource,
                indication = rememberRipple()
            ) {
                onClick?.invoke()
            }
            .onKeyEvent {
                if (!listOf(Key.DirectionCenter, Key.Enter).contains(it.key)) {
                    onFocusChanged?.invoke(isItemFocused)
                    when (it.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            onLeft?.invoke()
                            println("OnLEFT")
                        }
                        KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            onRight?.invoke()
                            println("OnRIGHT")
                        }
                        KeyEvent.KEYCODE_DPAD_UP -> {
                            onUp?.invoke()
                            println("OnUP")
                        }
                        KeyEvent.KEYCODE_DPAD_DOWN -> {
                            onDown?.invoke()
                            println("OnDOWN")
                        }
                    }
                    return@onKeyEvent false
                }
                when (it.type) {
                    KeyEventType.KeyDown -> {
                        val press =
                            PressInteraction.Press(
                                pressPosition = Offset(
                                    x = boxSize.width / 2f,
                                    y = boxSize.height / 2f
                                )
                            )
                        scope.launch {
                            boxInteractionSource.emit(press)
                        }
                        previousPress = press
                        true
                    }
                    KeyEventType.KeyUp -> {
                        previousPress?.let { previousPress ->
                            onClick?.invoke()
                            scope.launch {
                                boxInteractionSource.emit(
                                    PressInteraction.Release(
                                        press = previousPress
                                    )
                                )
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
            .focusable(interactionSource = boxInteractionSource)
            .border(
                width = borderWidth,
                color = animatedBorderColor
            ),
    ) {
        content(isItemFocused)
    }
}