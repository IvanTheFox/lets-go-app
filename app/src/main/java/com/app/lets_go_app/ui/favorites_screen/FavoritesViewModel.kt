package com.app.lets_go_app.ui.favorites_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState(isLoading = true))
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

//    init {
//        loadFavorites()
//    }

//    private fun loadFavorites() {
//        viewModelScope.launch {
//            repository.getFavoriteEvents().collect { events ->
//                _uiState.update { currentState ->
//                    currentState.copy(events = events, isLoading = false)
//                }
//            }
//        }
//    }

    fun toggleFavorite(eventId: Int) {
        repository.toggleFavorite(eventId)
    }
}