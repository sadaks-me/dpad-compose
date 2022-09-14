package dev.berggren.util

import androidx.activity.ComponentActivity
import dev.berggren.hideSystemUI

open class BaseActivity : ComponentActivity() {
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }
}