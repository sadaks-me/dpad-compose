package dev.berggren.ui.focusable

import android.view.KeyEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.berggren.highlightColor
import dev.berggren.trainerBgColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun Focusable(
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    onFocusChanged: ((isFocussed: Boolean) -> Unit)? = null,
    onLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    onUp: (() -> Unit)? = null,
    onDown: (() -> Unit)? = null,
    focusRequester: FocusRequester? = FocusRequester(),
    focusProperties: (FocusProperties.() -> Unit) = { },
    magnify: Boolean = true,
    visible: Boolean = true,
    cardElevation: Dp = 10.dp,
    cardBackgroundColor: Color = trainerBgColor,
    shape: Shape = RoundedCornerShape(4.dp),
    borderWidth: Dp = 2.dp,
    unfocusedBorderColor: Color = Color.Transparent,
    focusedBorderColor: Color = highlightColor,
    indication: Indication? = null,
    scrollPadding: Rect = Rect.Zero,
    isDefault: Boolean = false,
    content: @Composable (isFocused: Boolean) -> Unit,
) {

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val boxInteractionSource = remember { MutableInteractionSource() }
    val isItemFocused by boxInteractionSource.collectIsFocusedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (isItemFocused) 1f else (if (magnify) 0.85f else 1f)
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isItemFocused) focusedBorderColor else unfocusedBorderColor
    )
    var previousFocus: FocusInteraction.Focus? by remember {
        mutableStateOf(null)
    }
    var previousPress: PressInteraction.Press? by remember {
        mutableStateOf(null)
    }
    val scope = rememberCoroutineScope()

    var boxSize by remember {
        mutableStateOf(IntSize(0, 0))
    }
    val inputMode = LocalInputModeManager.current

    LaunchedEffect(inputMode.inputMode) {
        delay(10)
        if (enabled && isDefault) {
            focusRequester?.requestFocus()
        }
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

    val modifier = if (enabled) Modifier
        .graphicsLayer {
            scaleX = animatedScale
            scaleY = animatedScale
        }
        .bringIntoViewRequester(bringIntoViewRequester)
        .onSizeChanged {
            boxSize = it
        }
        .indication(
            interactionSource = boxInteractionSource,
            indication = indication ?: rememberRipple()
        )
        .onFocusChanged { focusState ->
            if (focusState.isFocused) {
                val newFocusInteraction = FocusInteraction.Focus()
                scope.launch {
                    boxInteractionSource.emit(newFocusInteraction)
                }
                scope.launch {
                    val visibilityBounds = Rect(
                        left = -1f * scrollPadding.left,
                        top = -1f * scrollPadding.top,
                        right = boxSize.width + scrollPadding.right,
                        bottom = boxSize.height + scrollPadding.bottom
                    )
                    bringIntoViewRequester.bringIntoView(visibilityBounds)
                }
                previousFocus = newFocusInteraction
            } else {
                previousFocus?.let {
                    scope.launch {
                        boxInteractionSource.emit(FocusInteraction.Unfocus(it))
                    }
                }
            }
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
        .border(
            width = if (isItemFocused) borderWidth else 0.dp,
            color = animatedBorderColor,
            shape = shape
        )
        .focusRequester(focusRequester?:FocusRequester())
        .focusProperties { focusProperties() }
        .focusTarget()
    else Modifier

    Box(
        modifier = modifier.alpha(if (visible) 1f else 0f),
    ) {
        ElevatedCard(
            shape = shape,
            elevation = CardDefaults.elevatedCardElevation(
                focusedElevation = cardElevation,
                defaultElevation = cardElevation
            ),
            colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
        ) {
            content(isItemFocused)
        }
    }
}