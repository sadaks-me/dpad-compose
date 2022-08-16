package dev.berggren

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

val rowColors = listOf(
    Color(0xff1abc9c),
    Color(0xff2ecc71),
    Color(0xff3498db),
    Color(0xff9b59b6),
    Color(0xff34495e)
)
const val itemsPerRow = 10
const val menuItems = 1
val boxColors = rowColors.map { rowColor ->
    (0..itemsPerRow).map { rowIndex ->
        val fraction = (1 - rowIndex.toFloat() / itemsPerRow)
        Color(
            red = fraction * rowColor.red,
            green = fraction * rowColor.green,
            blue = fraction * rowColor.blue
        )
    }
}

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun App() {
    val scope = rememberCoroutineScope()
    var colorFocussed: Color by remember { mutableStateOf(Color.Transparent) }
    var pageIndex: Int by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Menu(colorFocussed) {
                scope.launch {
                    colorFocussed = Color.Transparent
                    pageIndex = it
                    pagerState.animateScrollToPage(it)
                }
            }
            HorizontalPager(
                state = pagerState,
                count = menuItems,
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(tween(500, easing = LinearEasing))
            ) { page ->
                when (page) {
                    0 -> Home1 {
                        colorFocussed = it
                    }
                    1 -> Box(contentAlignment = Alignment.Center) {
                        Column(){
                            Text(
                                text = "Page: $page",
                                style = MaterialTheme.typography.h2.copy(Color.White)
                            )
                            ScrollableGrid(
                                items = boxColors,
                            ) { color ->
                                ColoredBox(
                                    color = color
                                )
                            }
                        }
                    }
                    else -> Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Page: $page",
                            style = MaterialTheme.typography.h2.copy(Color.White)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Home1(onColorChanged: ((color: Color) -> Unit)? = null) {
    val verticalScrollState = remember { ScrollState(initial = 0) }
    val pagerState = rememberPagerState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xffecf0f1))
            .padding(top = 24.dp)
            .verticalScroll(verticalScrollState)
    ) {
        HorizontalPager(
            state = pagerState,
            count = 2,
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .animateContentSize(tween(500, easing = LinearEasing))
        ) { page ->
            when (page) {
                0 ->
                    Focusable(
                        onFocusChanged = {
                            if (it) onColorChanged?.invoke(Color.Transparent)
                        }
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Cyan),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            ColoredBox(
                                height = 50.dp,
                                width = 250.dp,
                                color = if (it) rowColors.first() else Color.Gray
                            )
                        }
                    }
                1 -> Focusable(
                    onFocusChanged = {
                        if (it) onColorChanged?.invoke(Color.Transparent)
                    }
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Blue),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        ColoredBox(
                            height = 50.dp,
                            width = 250.dp,
                            color = rowColors.last()
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        ScrollableGrid(
            items = boxColors,
        ) { color ->
            ColoredBox(
                Modifier.dpadFocusable(
                    onFocusChanged = {
                        onColorChanged?.invoke(if (it) color else Color.Transparent)
                    }
                ),
                color = color
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Menu(colorFocussed: Color, onMenuFocussed: ((index: Int) -> Unit)? = null) {
    var menuIndex by remember { mutableStateOf(1) }
    Box(contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier
                .fillMaxHeight()
                .background(colorFocussed),
            verticalArrangement = Arrangement.Center
        ) {
            (0..menuItems).map { index ->
                ColoredBox(
                    Modifier.dpadFocusable(onFocusChanged = {
                        if (it) {
                            menuIndex = index
                            onMenuFocussed?.invoke(menuIndex)
                        }
                    }),
                    height = 50.dp,
                    width = 50.dp,
                    color = Color.Cyan
                )
            }
        }
    }
}

@Composable
fun ColoredBox(
    modifier: Modifier = Modifier,
    height: Dp = 128.dp,
    width: Dp = 128.dp,
    color: Color
) {
    Box(
        modifier
            .size(width, height)
            .background(color)
    )
}
