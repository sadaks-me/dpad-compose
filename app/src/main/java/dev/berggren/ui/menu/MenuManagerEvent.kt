package dev.berggren.ui.menu

sealed class MenuManagerEvent {
    object Open : MenuManagerEvent()
    object Close : MenuManagerEvent()
    object Hide : MenuManagerEvent()
}