package com.app.lets_go_app.ui.favorites_screen

import com.app.lets_go_app.model.Event

data class FavoritesUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false
)