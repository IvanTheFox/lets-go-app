package com.app.lets_go_app.ui.search_screen

import com.app.lets_go_app.model.Event

data class SearchUiState(
    val query: String = "",
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false
)