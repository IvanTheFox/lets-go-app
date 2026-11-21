package com.app.lets_go_app.ui.favorites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lets_go_app.model.Event
import com.app.lets_go_app.model.IRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: IRepository
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 10
    }

    private val _uiState = MutableStateFlow(FavoritesUiState(isLoading = true))
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Repository request
            val initialEvents: List<Event>? = repository.getFavoriteEvents(count = PAGE_SIZE)

            if (initialEvents == null) {
                // Repo call error
                _uiState.update { it.copy(isLoading = false) }
            } else {
                // Successful repo call
                _uiState.update { it.copy(events = initialEvents, isLoading = false) }
            }
        }
    }

    fun toggleFavorite(eventId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.setFavoriteEvent(eventId, isFavorite)
        }
    }
}