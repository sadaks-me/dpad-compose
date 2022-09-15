package dev.berggren

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import dev.berggren.ui.app.App
import dev.berggren.ui.app.LocalNavHostController
import dev.berggren.ui.menu.MenuManager
import dev.berggren.ui.navigation.Navigator
import dev.berggren.util.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var menuManager: MenuManager

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalNavHostController.Init()
            MaterialTheme {
                App(navigator, menuManager)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun PagerUi(onColorChanged: ((color: Color) -> Unit)? = null) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var isFocussed: Boolean = rememberSaveable { true }
    HorizontalPager(
        state = pagerState,
        count = 2,
        userScrollEnabled = false,
        modifier = Modifier
            .height(if (isFocussed) 400.dp else 200.dp)
            .fillMaxWidth()
            .animateContentSize(tween(500, easing = LinearEasing))
    ) { page ->
//        when (page) {
//            0 ->
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Cyan)
//                        .bringIntoViewRequester(bringIntoViewRequester)
//                        .animateContentSize(tween(500, easing = LinearEasing)),
//                    contentAlignment = Alignment.BottomStart
//                ) {
//                    Row(
//                        Modifier.padding(bottom = 25.dp, start = 10.dp),
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        Focusable(
//                            isDefault = true,
//                            onFocusChanged = {
//                                isFocussed = it
//                                if (it) {
//                                    scope.launch {
//                                        bringIntoViewRequester.bringIntoView(null)
//                                    }
//                                    onColorChanged?.invoke(Color.Transparent)
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 50.dp,
//                                width = 250.dp,
//                                color = if (it) rowColors.first() else Color.Gray
//                            )
//                        }
//                        Focusable(
//                            onLeft = {},
//                            onRight = { scope.launch { pagerState.animateScrollToPage(1) } },
//                            onFocusChanged = {
//                                isFocussed = it
//                                if (it) {
//                                    scope.launch {
//                                        bringIntoViewRequester.bringIntoView(null)
//                                    }
//                                    onColorChanged?.invoke(Color.Transparent)
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 50.dp,
//                                width = 250.dp,
//                                color = if (it) rowColors.last() else Color.Gray
//                            )
//                        }
//                    }
//                }
//            1 ->
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Magenta)
//                        .animateContentSize(tween(500, easing = LinearEasing)),
//                    contentAlignment = Alignment.BottomStart
//                ) {
//                    Row(
//                        Modifier.padding(bottom = 25.dp, start = 10.dp),
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        Focusable(
//                            onLeft = { scope.launch { pagerState.animateScrollToPage(0) } },
//                            onFocusChanged = {
//                                isFocussed = it
//                                if (it) {
//                                    scope.launch {
//                                        bringIntoViewRequester.bringIntoView(null)
//                                    }
//                                    onColorChanged?.invoke(Color.Transparent)
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 50.dp,
//                                width = 250.dp,
//                                color = if (it) boxColors.first().last() else Color.Gray
//                            )
//                        }
//                        Focusable(
//                            onLeft = {},
//                            onFocusChanged = {
//                                isFocussed = it
//                                if (it) {
//                                    scope.launch {
//                                        bringIntoViewRequester.bringIntoView(null)
//                                    }
//                                    onColorChanged?.invoke(Color.Transparent)
//                                }
//                            }
//                        ) {
//                            ColoredBox(
//                                height = 50.dp,
//                                width = 250.dp,
//                                color = if (it) boxColors.first().first() else Color.Gray
//                            )
//                        }
//                    }
//                }
//        }
    }
}
