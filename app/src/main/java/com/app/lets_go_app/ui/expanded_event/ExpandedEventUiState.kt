package com.app.lets_go_app.ui.components // или в отдельной папке expanded_event_screen

import com.app.lets_go_app.model.Event

data class ExpandedEventUiState(
    val event: Event? = null,
    val isLoading: Boolean = false
)