package com.app.lets_go_app.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class NavigationState(
    activeSection: MutableState<MainAppSection>,
    val backStacks: Map<MainAppSection, NavBackStack<NavKey>>
) {
    var activeSection by activeSection
    val currentBackStack: NavBackStack<NavKey>
        get() = backStacks[activeSection] ?: error("Tried to switch to an unknown app section.")
}