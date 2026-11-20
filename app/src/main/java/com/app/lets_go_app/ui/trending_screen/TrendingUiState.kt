package com.app.lets_go_app.ui.trending_screen

import com.app.lets_go_app.model.Event

data class TrendingUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false
)