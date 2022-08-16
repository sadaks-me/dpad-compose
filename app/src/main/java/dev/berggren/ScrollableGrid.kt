package dev.berggren

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun <T> ScrollableGrid(
    items: List<List<T>>,
    contentForItem: @Composable BoxScope.(item: T) -> Unit
) {
        items.forEach { rowItems ->
            val rowScrollState = remember { ScrollState(initial = 0) }
            Column(){
                Text(text = "Row: ${items.indexOf(rowItems)}", style = MaterialTheme.typography.h4)
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .horizontalScroll(rowScrollState)
                ) {
                    rowItems.forEach { rowItem ->
                        Row {
                            Box {
                                contentForItem(rowItem)
                            }
                            Spacer(Modifier.width(24.dp))
                        }
                    }
                }
            }
        }
}
