package dev.berggren.ui.menu

import kotlinx.coroutines.flow.Flow

interface MenuManager {
    fun open(): Boolean
    fun close(): Boolean
    fun hide(): Boolean
    var isOpen: Boolean
    val events: Flow<MenuManagerEvent>
}
