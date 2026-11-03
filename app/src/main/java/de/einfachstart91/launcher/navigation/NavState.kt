package de.einfachstart91.launcher.navigation

import androidx.compose.runtime.mutableStateListOf
import de.einfachstart91.launcher.data.PinMode

sealed interface Screen {
    data object Home : Screen
    data class Pin(val mode: PinMode) : Screen
    data object Admin : Screen
    data class ContactEditor(val contactId: String?) : Screen
    data object Emergency : Screen
    data object Help : Screen
    data object Phone : Screen
    data object Messages : Screen
    data class Info(val title: String, val message: String) : Screen
}

class NavState {
    val backStack = mutableStateListOf<Screen>(Screen.Home)
    val current: Screen get() = backStack.lastOrNull() ?: Screen.Home

    fun navigate(screen: Screen) {
        backStack.add(screen)
    }

    fun replaceTop(screen: Screen) {
        if (backStack.isEmpty()) navigate(screen)
        else backStack[backStack.lastIndex] = screen
    }

    fun back() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    fun reset() {
        backStack.clear()
        backStack.add(Screen.Home)
    }
}
