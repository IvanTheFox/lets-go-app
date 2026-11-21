package com.app.lets_go_app.model

interface IRepository {
    // Getters
    suspend fun getEventById(id: Int): Event?
    suspend fun getTrendingEvents(count: Int): List<Event>?
    suspend fun getTrendingEventsFrom(fromId: Int, count: Int): List<Event>?
    suspend fun getFavoriteEvents(count: Int): List<Event>?
    suspend fun getFavoriteEventsFrom(fromId: Int, count: Int): List<Event>?
    suspend fun getFilteredEvents(filter: EventFilter, count: Int): List<Event>?
    suspend fun getFilteredEventsFrom(filter: EventFilter, fromId: Int, count: Int): List<Event>?
    suspend fun isFavoriteEvent(id: Int): Boolean?

    // Setters
    suspend fun setFavoriteEvent(eventId: Int, isFavorite: Boolean): Boolean
    suspend fun updateFavoriteEvents(): Boolean
}