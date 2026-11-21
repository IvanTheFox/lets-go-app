package com.app.lets_go_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.lets_go_app.ui.favorites_screen.FavoritesViewModel
import com.app.lets_go_app.ui.search_screen.SearchViewModel
import com.app.lets_go_app.ui.trending_screen.TrendingViewModel

class ViewModelFactory(
    private val container: DependencyContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            TrendingViewModel::class.java ->
                TrendingViewModel(container.repository) as T

            SearchViewModel::class.java ->
                SearchViewModel(container.repository) as T

            FavoritesViewModel::class.java ->
                FavoritesViewModel(container.repository) as T

            else -> throw IllegalArgumentException("Unknown view-model type: $modelClass")
        }
    }
}