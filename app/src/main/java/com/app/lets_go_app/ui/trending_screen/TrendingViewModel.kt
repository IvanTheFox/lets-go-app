package com.app.lets_go_app.ui.trending_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lets_go_app.model.Event
import com.app.lets_go_app.model.IRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrendingViewModel(
    private val repository: IRepository
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 10
    }

    private val _uiState = MutableStateFlow(TrendingUiState(isLoading = true))
    val uiState: StateFlow<TrendingUiState> = _uiState.asStateFlow()

    init {
        loadTrendingEvents()
    }

    private fun loadTrendingEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Repository request
            val initialEvents: List<Event>? = repository.getTrendingEvents(count = PAGE_SIZE)

            if (initialEvents == null) {
                // Repo call error
                _uiState.update { it.copy(isLoading = false) }
            } else {
                // Successful repo call
                _uiState.update { it.copy(events = initialEvents, isLoading = false) }
            }
        }
    }

    fun loadMoreTrendingCards() {
        if (_uiState.value.isLoading) return

        val currentEvents = _uiState.value.events

        val lastEventId = currentEvents.lastOrNull()?.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Repository request
            val moreEvents: List<Event>? = repository.getTrendingEventsFrom(fromId = lastEventId, count = PAGE_SIZE)

            if (moreEvents == null) {
                // Repo call Error
                _uiState.update { it.copy(isLoading = false) }
            } else {
                // Successful repo call
                _uiState.update { currentState ->
                    currentState.copy(
                        events = currentState.events + moreEvents,
                        isLoading = false
                    )
                }
            }
        }
    }
}