package com.app.lets_go_app.model

interface IRepository {
    // Getters
    fun getTrendingEvents(count: Int): List<Event>?
    fun getTrendingEventsFrom(fromId: Int, count: Int): List<Event>?
    fun getFavoriteEvents(count: Int): List<Event>?
    fun getFavoriteEventsFrom(fromId: Int, count: Int): List<Event>?
    fun getFilteredEvents(filter: EventFilter, count: Int): List<Event>?
    fun getFilteredEventsFrom(filter: EventFilter, count: Int): List<Event>?

    // Setters
    fun setFavoriteEvent(eventId: Int, isFavorite: Boolean)
    fun updateFavoriteEvents(): Boolean
}