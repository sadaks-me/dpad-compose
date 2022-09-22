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
