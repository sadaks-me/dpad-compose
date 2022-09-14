package dev.berggren

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState

@ExperimentalComposeUiApi
@Composable
fun <T> ScrollableGrid(
    modifier: Modifier = Modifier,
    items: List<List<T>>,
    contentForItem: @Composable BoxScope.(item: T, position: GridPosition) -> Unit
) {
    val verticalScrollState = remember { ScrollState(initial = 0) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {
        items.forEachIndexed { rowIndex, rowItems ->
            val rowScrollState = remember { ScrollState(initial = 0) }
            Column(
                Modifier.padding(
                    top = if (rowIndex == 0) 12.dp else 0.dp,
                )
            ) {
                Column(Modifier.height(40.dp)) {

                    Text(
                        text = "I'm row number: ${items.indexOf(rowItems)}",
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
                    rowItems.forEachIndexed { columnIndex, rowItem ->
                        Box(
                            Modifier.padding(
                                start = if (columnIndex == 0) 12.dp else 0.dp,
                                end = if (columnIndex == rowItems.size - 1) 12.dp else 0.dp
                            )
                        ) {
                            contentForItem(
                                rowItem, GridPosition(
                                    rowIndex = rowIndex,
                                    columnIndex = columnIndex
                                )
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(50.dp))
    }
}

@Stable
data class GridPosition(val rowIndex: Int, val columnIndex: Int)

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
