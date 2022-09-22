package dev.berggren.ui.menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.berggren.*
import dev.berggren.R.drawable
import dev.berggren.ui.focusable.Focusable
import dev.berggren.ui.navigation.HomeScreen
import dev.berggren.ui.navigation.Screen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Menu(
    selectedItem: MenuOptionEnum? = null,
    menuState: MenuStateEnum
) {
    val focusManager = LocalFocusManager.current

    val animatedWidth by animateDpAsState(
        targetValue = when (menuState) {
            MenuStateEnum.HIDDEN -> 0.dp
            MenuStateEnum.CLOSED -> 50.dp
            MenuStateEnum.OPEN -> 143.dp
        },
        animationSpec = tween(250, easing = LinearEasing)
    )

    val viewModel = hiltViewModel<MenuViewModel>()

    val expanded = if (menuState == MenuStateEnum.HIDDEN) false else menuState == MenuStateEnum.OPEN

    val selected = viewModel.selectedMenuOption.observeAsState()

    viewModel.setSelectedMenu(selectedItem)

    LaunchedEffect(menuState) {
        if (menuState == MenuStateEnum.OPEN)
            focusManager.moveFocus(FocusDirection.Enter)
    }

    val focusRequesters = remember(menuItems) { Array(menuItems.size) { FocusRequester() } }
    Box(
        Modifier
            .fillMaxHeight()
            .width(animatedWidth)
            .background(if (expanded) darkGradientColor else Color.Transparent)
            .onFocusChanged {
                if (it.isFocused && menuState != MenuStateEnum.OPEN) {
                    viewModel.menuManager.open()
                }
            }
            .focusProperties {
                enter =
                    { focusRequesters[menuItems.indexOf(menuItems.first { it.menuOptionEnum == selected.value!! })] }
            }, contentAlignment = Alignment.Center
    ) {
        Column {
            menuItems.forEachIndexed { i, item ->
                Box(modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp)) {
                    Focusable(enabled = menuState == MenuStateEnum.OPEN,
                        focusRequester = focusRequesters[i],
                        cardElevation = 0.dp,
                        onClick = {
                            viewModel.selectMenuOption(
                                menuItemModel = menuItems[i],
                            )
                            focusManager.moveFocus(FocusDirection.Right)
                        }) {
                        val isSelected = selected.value!! == item.menuOptionEnum
                        MenuItem(isSelected, expanded, item)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    isSelected: Boolean,
    isExpanded: Boolean, item: MenuItemModel
) {
    val iconTint = when {
        isSelected && isExpanded.not() -> DashboardBackground
        isSelected && isExpanded -> DashboardBackground
        else -> Color.White
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(250, easing = LinearEasing)
    )
    Row(
        Modifier
            .height(30.dp)
            .width(143.dp)
            .background(DashboardBackground),
        horizontalArrangement = Arrangement.Start
    ) {
        Box {
            ColoredBox(
                height = 30.dp,
                width = 30.dp,
                color = if (isSelected) highlightColor else trainerBgColor
            )
            if (item.iconId != 0)
                Icon(
                    painter = painterResource(id = item.iconId),
                    modifier = Modifier.size(30.dp),
                    tint = iconTint.copy(alpha = if (isSelected.not()) 0.6f else 1f),
                    contentDescription = ""
                )
        }
        Box(
            Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically)
                .alpha(animatedAlpha)
        ) {
            Text(
                item.label,
                Modifier.padding(horizontal = 6.dp),
                maxLines = 1,
                style = TextStyle(color = Color.White)
            )
        }
    }
}

enum class MenuStateEnum {
    OPEN,
    CLOSED,
    HIDDEN,
}

enum class MenuOptionEnum {
    HOME,
    WORKOUTS,
    ACADEMY,
    PROGRAMME,
    PROFILE,
}

data class MenuItemModel(
    val menuOptionEnum: MenuOptionEnum,
    val route: String,
    val iconId: Int,
    val label: String,
    val focusRequester: FocusRequester
)

val homeMenuItem = MenuItemModel(
    MenuOptionEnum.HOME,
    HomeScreen.Dash.route,
    drawable.ic_home,
    "Home",
    FocusRequester()
)

val workoutMenuItem = MenuItemModel(
    MenuOptionEnum.WORKOUTS,
    Screen.Empty.route,
    drawable.ic_workouts,
    "Workouts",
    FocusRequester()
)

val academyMenuItem = MenuItemModel(
    MenuOptionEnum.ACADEMY,
    Screen.Empty.route,
    drawable.ic_practice,
    "Practices",
    FocusRequester()
)

val programmeMenuItem = MenuItemModel(
    MenuOptionEnum.PROGRAMME,
    Screen.Empty.route,
    drawable.ic_new_programme,
    "Programmes",
    FocusRequester()
)

val profileMenuItem = MenuItemModel(
    MenuOptionEnum.PROFILE,
    Screen.Empty.route,
    drawable.ic_new_selected_profile,
    "Profile",
    FocusRequester()
)

val menuItems = mutableListOf(
    homeMenuItem,
    workoutMenuItem,
    academyMenuItem,
    programmeMenuItem,
    profileMenuItem
)