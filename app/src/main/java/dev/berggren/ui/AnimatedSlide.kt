package dev.berggren.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedSlide(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    hideContentIfNotEnabled: Boolean = true,
    slideDirection: AnimatedContentScope.SlideDirection = AnimatedContentScope.SlideDirection.Right,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        enabled,
        modifier = modifier,
        contentAlignment = contentAlignment,
        transitionSpec = {
            when (slideDirection) {
                AnimatedContentScope.SlideDirection.Left -> {
                    if (targetState) {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width } + fadeOut()
                    }.using(SizeTransform(clip = false))
                }
                AnimatedContentScope.SlideDirection.Down -> {
                    if (targetState) {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }
                }
                AnimatedContentScope.SlideDirection.Up -> {
                    if (targetState) {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    } else {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    }
                }
                else -> {
                    if (targetState) {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally { width -> -width } + fadeOut()
                    }
                }
            }.using(SizeTransform(clip = false))
        },
    ) { shouldShow ->
        if (hideContentIfNotEnabled && !shouldShow)
            Box {}
        else
            Box(Modifier.alpha(if (shouldShow) 1f else 0f)) { content() }
    }
}