package com.app.lets_go_app.navigation

import androidx.navigation3.runtime.NavKey
import com.app.lets_go_app.R
import kotlinx.serialization.Serializable

@Serializable
sealed class MainAppSection(
    val nameId: Int,
    val iconId: Int
): Destination

@Serializable
sealed interface Destination: NavKey {
    @Serializable
    object TrendingAppSection :
        MainAppSection(R.string.trending_section, R.drawable.trending)
    @Serializable
    object SearchAppSection :
        MainAppSection(R.string.search_section, R.drawable.search)
    @Serializable
    object FavoritesAppSection :
        MainAppSection(R.string.favorite_section, R.drawable.favorites)
    @Serializable
    class ExpandedEventCard(val eventId: Int) : Destination
}