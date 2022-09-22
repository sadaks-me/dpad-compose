package dev.berggren.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dev.berggren.*
import dev.berggren.ui.focusable.Focusable
import dev.berggren.ui.menu.MenuStateEnum
import dev.berggren.ui.menu.MenuViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Rails(
    items: List<SectionModel>,
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
        HeroBackground(320.dp)
        Hero(320.dp, enabled = focussedY == null || focussedY == 0 )
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 320.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollableGrid(
                models = items,
                selectedGrid = GridPosition(focussedY, focussedX),
                onItemSelected = {
                    onItemSelected?.invoke()
                }
            ) { position ->
                focussedY = position.yIndex
                focussedX = position.xIndex
            }
        }
    }
}

@Composable
fun HeroBackground(topPadding: Dp) {
    Box(
        modifier = Modifier
            .height(topPadding)
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Hero(
    topPadding: Dp,
    enabled: Boolean = true
) {
    Box(
        Modifier
            .height(topPadding)
            .fillMaxWidth()
            .background(Color.Transparent)
            .focusProperties { up = FocusRequester.Cancel },
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            Modifier
                .padding(bottom = 30.dp, start = 20.dp)
                .alpha(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            Focusable(
                enabled,
                visible = enabled,
                isDefault = true,
                borderWidth = 0.dp,
                magnify = false,
            ) {
                ColoredBox(
                    height = 40.dp,
                    width = 200.dp,
                    color = if (it) highlightColor else trainerBgColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Focusable(
                enabled,
                visible = enabled,
                borderWidth = 0.dp,
                magnify = false,
            ) {
                ColoredBox(
                    height = 40.dp,
                    width = 200.dp,
                    color = if (it) highlightColor else trainerBgColor
                )
            }
        }
    }
}

//@OptIn(
//    ExperimentalPagerApi::class,
//    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class
//)
//@Composable
//fun PagerHero() {
//    val focusManager = LocalFocusManager.current
//    val pagerState = rememberPagerState()
//    val scope = rememberCoroutineScope()
//    val (item1, item2, item3, item4) = remember { FocusRequester.createRefs() }
//    val bringIntoViewRequester1 = remember { BringIntoViewRequester() }
//    val bringIntoViewRequester2 = remember { BringIntoViewRequester() }
//    LaunchedEffect(pagerState.currentPage) {
//        println("pagerState.currentPage: ${pagerState.currentPage}")
////        if (pagerState.currentPage == 0) item1.requestFocus() else item3.requestFocus()
//    }
//
//    HorizontalPager(
//        state = pagerState,
//        count = 2,
//        userScrollEnabled = false,
//        modifier = Modifier
//            .wrapContentHeight()
//            .fillMaxWidth()
//            .animateContentSize(tween(500, easing = LinearEasing))
//            .focusProperties {
//                up = FocusRequester.Cancel
//            },
//    ) { page ->
//        when (page) {
//            0 -> {
//                Box(
//                    Modifier
//                        .height(320.dp)
//                        .padding(bottom = 24.dp)
//                        .fillMaxWidth()
//                        .bringIntoViewRequester(bringIntoViewRequester1)
//                        .background(Color.Transparent),
//                    contentAlignment = Alignment.BottomStart
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .align(Alignment.BottomCenter)
//                            .fillMaxHeight(1f)
//                            .fillMaxWidth()
//                            .background(
//                                brush = Brush.verticalGradient(
//                                    colors = listOf(
//                                        Color.Transparent,
//                                        darkGradientColor,
//                                    ),
//                                )
//                            )
//                    )
//                    Row(
//                        Modifier
//                            .padding(bottom = 30.dp, start = 20.dp)
//                            .alpha(1f),
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        Focusable(
//                            borderWidth = 0.dp,
//                            magnify = false,
//                            focusRequester = item1,
//                            focusProperties = {
//                                right = item2
//                            },
//                            onFocusChanged = {
//                                if (it) {
//                                    println("Page 1 Button 1 has focus")
//                                } else {
//                                    println("Page 1 Button 1 lost focus")
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 40.dp,
//                                width = 200.dp,
//                                color = if (it) highlightColor else trainerBgColor
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(12.dp))
//                        Focusable(
//                            borderWidth = 0.dp,
//                            magnify = false,
//                            focusRequester = item2,
//                            focusProperties = {
//                                left = item1
//                                right = item3
//                            },
//                            onRight = {
//                                println("On Right presseddddd")
//                                if (pagerState.currentPage != 1)
//                                    scope.launch {
//                                        pagerState.animateScrollToPage(1)
////                                        bringIntoViewRequester2.bringIntoView(null)
//                                    }
//                            },
//                            onFocusChanged = {
//                                if (it) {
//                                    println("Page 1 Button 2 has focus")
//                                } else {
//                                    println("Page 1 Button 2 lost focus")
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 40.dp,
//                                width = 200.dp,
//                                color = if (it) highlightColor else trainerBgColor
//                            )
//                        }
//                    }
//                }
//            }
//            1 ->
//                Box(
//                    Modifier
//                        .height(320.dp)
//                        .padding(bottom = 24.dp)
//                        .fillMaxWidth()
//                        .bringIntoViewRequester(bringIntoViewRequester2)
//                        .background(Color.Gray),
//                    contentAlignment = Alignment.BottomStart
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .align(Alignment.BottomCenter)
//                            .fillMaxHeight(1f)
//                            .fillMaxWidth()
//                            .background(
//                                brush = Brush.verticalGradient(
//                                    colors = listOf(
//                                        Color.Transparent,
//                                        darkGradientColor,
//                                    ),
//                                )
//                            )
//                    )
//                    Row(
//                        Modifier
//                            .padding(bottom = 30.dp, start = 20.dp)
//                            .alpha(1f),
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        Focusable(
//                            borderWidth = 0.dp,
//                            magnify = false,
//                            focusRequester = item3,
//                            focusProperties = {
//                                left = item2
//                                right = item4
//                            },
//                            onLeft = {
//                                println("On Left presseddddd")
//                                if (pagerState.currentPage != 0)
//                                    scope.launch {
//                                        pagerState.animateScrollToPage(0)
////                                        bringIntoViewRequester1.bringIntoView(null)
//                                    }
//                            },
//                            onFocusChanged = {
//                                if (it) {
//                                    println("Page 2 Button 1 has focus")
//                                } else {
//                                    println("Page 2 Button 1 lost focus")
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 40.dp,
//                                width = 200.dp,
//                                color = if (it) highlightColor else trainerBgColor
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(12.dp))
//                        Focusable(
//                            borderWidth = 0.dp,
//                            magnify = false,
//                            focusRequester = item4,
//                            focusProperties = {
//                                left = item3
//                                right = FocusRequester.Cancel
//                            },
//                            onFocusChanged = {
//                                if (it) {
//                                    println("Page 2 Button 2 has focus")
//                                } else {
//                                    println("Page 2 Button 2 lost focus")
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 40.dp,
//                                width = 200.dp,
//                                color = if (it) highlightColor else trainerBgColor
//                            )
//                        }
//                    }
//                }
//        }
//    }
//}