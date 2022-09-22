package dev.berggren

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.berggren.ui.RailItem
import dev.berggren.ui.SectionModel
import dev.berggren.ui.focusable.Focusable
import dev.berggren.ui.menu.menuItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun ScrollableGrid(
    models: List<SectionModel>,
    selectedGrid: GridPosition,
    onItemSelected: (RailItem) -> Unit,
    onIndexChanged: (position: GridPosition) -> Unit
) {

    val verticalScrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {
        val focusRequesters = remember(models) { Array(models.size) { FocusRequester() } }
        models.forEachIndexed { yIndex, model ->
            ScrollableRow(
                model = model,
                focusRequester = focusRequesters[yIndex],
                yIndex = yIndex,
                selectedGrid = selectedGrid,
                onItemSelected
            ) { pos ->
                onIndexChanged(pos)
            }
        }
        Spacer(Modifier.height(50.dp))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScrollableRow(
    model: SectionModel,
    focusRequester: FocusRequester,
    yIndex: Int,
    selectedGrid: GridPosition,
    onItemSelected: (RailItem) -> Unit,
    onIndexChanged: (position: GridPosition) -> Unit
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val isFocussed = yIndex == selectedGrid.yIndex
    val rowScrollState = remember { ScrollState(initial = 0) }
    val focusRequesters = remember(model.items) { Array(model.items.size) { FocusRequester() } }

    var storedX: Int? by rememberSaveable {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = isFocussed) {
        scope.launch {
            if (isFocussed){
                println("isFocussed and storedX: $storedX")
                focusRequesters[storedX?:0].requestFocus()
            }
        }
    }

    Column(
        Modifier
            .padding(
                top = if (yIndex == 0) 12.dp else 0.dp,
            )
            .background(if (isFocussed) Color.Gray else transparent)
//            .focusRequester(focusRequester)
//            .focusProperties { enter = { focusRequesters[storedX ?: 0] } }
//            .focusTarget()
    ) {
        Column(Modifier.height(40.dp)) {
            Text(
                text = model.name,
                Modifier.padding(start = 20.dp),
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.h5.copy(color = Color.White.copy(0.8f))
            )
            Spacer(Modifier.height(4.dp))
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .horizontalScroll(rowScrollState)
        ) {
            model.items.forEachIndexed { xIndex, item ->
                Box(
                    Modifier.padding(
                        start = if (xIndex == 0) 12.dp else 0.dp,
                        end = if (xIndex == model.items.size - 1) 12.dp else 0.dp
                    )
                ) {
                    val elementPaddingAndHalfOfNextBox = with(LocalDensity.current) {
                        (boxPadding + (boxSize).div(4)).toPx()
                    }
                    Focusable(
                        focusRequester = focusRequesters[xIndex],
                        onClick = {
                            onItemSelected.invoke(item)
                        },
                        scrollPadding = Rect(
                            left = elementPaddingAndHalfOfNextBox,
                            top = elementPaddingAndHalfOfNextBox,
                            right = elementPaddingAndHalfOfNextBox,
                            bottom = elementPaddingAndHalfOfNextBox
                        ),
                        onFocusChanged = {
                            if (it) {
                                storedX = xIndex
                                onIndexChanged(GridPosition(yIndex, xIndex))
                                println("Selected index: position.xIndex: $yIndex - position.yIndex: $xIndex")
                            }
                        }
                    ) {
//                    val size = when (item) {
//                        is Trainer -> 128.dp
//                        is Programme -> 200.dp
//                        else -> 128.dp
//                    }
                        ColoredBox(color = transparent)
                        Text("$xIndex")
                    }
                }
            }
        }
    }
}

@Stable
data class GridPosition(val yIndex: Int?, val xIndex: Int?)

@Composable
fun ColoredBox(
    modifier: Modifier = Modifier,
    height: Dp = boxSize,
    width: Dp = boxSize,
    color: Color
) {
    Box(
        modifier = modifier
            .size(width, height)
            .background(color),
    )
}

internal val boxSize = 128.dp
internal val boxPadding = 12.dp
