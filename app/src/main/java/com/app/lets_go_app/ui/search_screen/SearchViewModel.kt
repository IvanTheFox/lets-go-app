package com.app.lets_go_app.ui.search_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState(isLoading = true))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

//    fun onQueryChange(newQuery: String) {
//        _uiState.update { it.copy(query = newQuery) }
//        searchEvents(newQuery)
//    }
//
//    private fun searchEvents(query: String) {
//        viewModelScope.launch {
//            repository.searchEvents(query).collect { events ->
//                _uiState.update { currentState ->
//                    currentState.copy(events = events, isLoading = false)
//                }
//            }
//        }
//    }
}