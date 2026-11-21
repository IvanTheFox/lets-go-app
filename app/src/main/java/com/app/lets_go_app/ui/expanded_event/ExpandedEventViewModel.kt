package com.app.lets_go_app.ui.components // или в отдельной папке

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.app.lets_go_app.LetsGoApp
import com.app.lets_go_app.model.IRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpandedEventViewModel(
    private val repository: IRepository,
    private val eventId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpandedEventUiState(isLoading = true))
    val uiState: StateFlow<ExpandedEventUiState> = _uiState.asStateFlow()

    init {
        loadEvent()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val event = repository.getEventById(eventId)

            if (event == null) {
                // Repo call error
                _uiState.update { it.copy(isLoading = false) }
            } else {
                // Successful repo call
                _uiState.update { it.copy(event = event, isLoading = false) }
            }
        }
    }

    companion object {
        fun factory(eventId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LetsGoApp
                ExpandedEventViewModel(
                    repository = app.container.repository,
                    eventId = eventId
                )
            }
        }
    }
}