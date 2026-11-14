package com.app.lets_go_app.navigation

class Navigator(val state: NavigationState) {
    fun navigate(destination: Destination) {
        if (destination is MainAppSection) {
            state.activeSection = destination
        } else {
            state.currentBackStack.add(destination)
        }
    }

    fun goBack() {
        state.currentBackStack.removeLastOrNull()
    }
}