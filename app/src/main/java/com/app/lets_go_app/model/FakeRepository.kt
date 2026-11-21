package com.app.lets_go_app.model

import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

class FakeRepository : IRepository {
    companion object {
        val localFavorites = listOf(2, 4, 7, 9, 11, 13, 16, 19, 21, 23, 26, 28, 31, 34, 36, 39, 41, 42, 45, 48, 50).toMutableList()
        val eventDataBase = listOf(
            // MOVIE
            Event(1, "Inception", EventType.MOVIE, 12.50, Instant.parse("2024-01-15T19:00:00Z"), 245, localFavorites.contains(1)),
            Event(2, "The Dark Knight", EventType.MOVIE, 11.00, Instant.parse("2024-01-16T20:30:00Z"), 312, localFavorites.contains(2)),
            Event(3, "Interstellar", EventType.MOVIE, 13.75, Instant.parse("2024-01-17T18:00:00Z"), 189, localFavorites.contains(3)),
            Event(4, "Oppenheimer", EventType.MOVIE, 15.00, Instant.parse("2024-01-18T21:00:00Z"), 421, localFavorites.contains(4)),
            Event(5, "Dune: Part Two", EventType.MOVIE, 14.50, Instant.parse("2024-01-19T19:45:00Z"), 388, localFavorites.contains(5)),
            Event(6, "Parasite", EventType.MOVIE, 10.25, Instant.parse("2024-01-20T17:30:00Z"), 156, localFavorites.contains(6)),
            Event(7, "Whiplash", EventType.MOVIE, 9.99, Instant.parse("2024-01-21T20:00:00Z"), 203, localFavorites.contains(7)),
            Event(8, "The Grand Budapest Hotel", EventType.MOVIE, 11.50, Instant.parse("2024-01-22T18:15:00Z"), 98, localFavorites.contains(8)),
            Event(9, "Spirited Away", EventType.MOVIE, 8.75, Instant.parse("2024-01-23T16:00:00Z"), 267, localFavorites.contains(9)),
            Event(10, "Blade Runner 2049", EventType.MOVIE, 13.00, Instant.parse("2024-01-24T21:30:00Z"), 174, localFavorites.contains(10)),

            // CONCERT
            Event(11, "Coldplay Live", EventType.CONCERT, 89.99, Instant.parse("2024-02-10T20:00:00Z"), 1523, localFavorites.contains(11)),
            Event(12, "Arctic Monkeys", EventType.CONCERT, 65.00, Instant.parse("2024-02-11T19:30:00Z"), 987, localFavorites.contains(12)),
            Event(13, "Taylor Swift Eras Tour", EventType.CONCERT, 149.99, Instant.parse("2024-02-12T18:00:00Z"), 4562, localFavorites.contains(13)),
            Event(14, "Radiohead Reunion", EventType.CONCERT, 120.00, Instant.parse("2024-02-13T20:15:00Z"), 2103, localFavorites.contains(14)),
            Event(15, "Jazz Night with Miles", EventType.CONCERT, 45.50, Instant.parse("2024-02-14T19:00:00Z"), 342, localFavorites.contains(15)),
            Event(16, "Metallica World Tour", EventType.CONCERT, 99.00, Instant.parse("2024-02-15T21:00:00Z"), 1876, localFavorites.contains(16)),
            Event(17, "Billie Eilish", EventType.CONCERT, 75.25, Instant.parse("2024-02-16T19:45:00Z"), 1234, localFavorites.contains(17)),
            Event(18, "Symphony Orchestra", EventType.CONCERT, 55.00, Instant.parse("2024-02-17T18:30:00Z"), 219, localFavorites.contains(18)),
            Event(19, "Daft Punk Tribute", EventType.CONCERT, 40.00, Instant.parse("2024-02-18T22:00:00Z"), 567, localFavorites.contains(19)),
            Event(20, "Foo Fighters", EventType.CONCERT, 85.75, Instant.parse("2024-02-19T20:00:00Z"), 1432, localFavorites.contains(20)),

            // EXHIBITION
            Event(21, "Van Gogh Immersive", EventType.EXHIBITION, 25.00, Instant.parse("2024-03-01T10:00:00Z"), 876, localFavorites.contains(21)),
            Event(22, "Modern Art Masters", EventType.EXHIBITION, 18.50, Instant.parse("2024-03-02T11:00:00Z"), 432, localFavorites.contains(22)),
            Event(23, "Ancient Egypt Treasures", EventType.EXHIBITION, 30.00, Instant.parse("2024-03-03T09:30:00Z"), 654, localFavorites.contains(23)),
            Event(24, "Photography: Wild Nature", EventType.EXHIBITION, 15.00, Instant.parse("2024-03-04T12:00:00Z"), 234, localFavorites.contains(24)),
            Event(25, "Sculpture Garden", EventType.EXHIBITION, 12.75, Instant.parse("2024-03-05T14:00:00Z"), 178, localFavorites.contains(25)),
            Event(26, "Space Exploration", EventType.EXHIBITION, 22.00, Instant.parse("2024-03-06T10:30:00Z"), 543, localFavorites.contains(26)),
            Event(27, "Renaissance Art", EventType.EXHIBITION, 28.50, Instant.parse("2024-03-07T11:15:00Z"), 321, localFavorites.contains(27)),
            Event(28, "Digital Art & AI", EventType.EXHIBITION, 20.00, Instant.parse("2024-03-08T13:00:00Z"), 765, localFavorites.contains(28)),
            Event(29, "Historical Artifacts", EventType.EXHIBITION, 16.25, Instant.parse("2024-03-09T09:00:00Z"), 289, localFavorites.contains(29)),
            Event(30, "Impressionist Paintings", EventType.EXHIBITION, 24.00, Instant.parse("2024-03-10T15:30:00Z"), 412, localFavorites.contains(30)),

            // STAND_UP
            Event(31, "Dave Chappelle Live", EventType.STAND_UP, 55.00, Instant.parse("2024-04-01T20:00:00Z"), 1234, localFavorites.contains(31)),
            Event(32, "Kevin Hart Comedy", EventType.STAND_UP, 48.50, Instant.parse("2024-04-02T21:00:00Z"), 987, localFavorites.contains(32)),
            Event(33, "Ricky Gervais", EventType.STAND_UP, 42.00, Instant.parse("2024-04-03T19:30:00Z"), 654, localFavorites.contains(33)),
            Event(34, "Bo Burnham Special", EventType.STAND_UP, 38.75, Instant.parse("2024-04-04T20:15:00Z"), 1876, localFavorites.contains(34)),
            Event(35, "Local Comedy Night", EventType.STAND_UP, 15.00, Instant.parse("2024-04-05T19:00:00Z"), 123, localFavorites.contains(35)),
            Event(36, "John Mulaney", EventType.STAND_UP, 50.00, Instant.parse("2024-04-06T21:30:00Z"), 876, localFavorites.contains(36)),
            Event(37, "Ali Wong", EventType.STAND_UP, 46.25, Instant.parse("2024-04-07T20:00:00Z"), 543, localFavorites.contains(37)),
            Event(38, "Improv Showcase", EventType.STAND_UP, 12.50, Instant.parse("2024-04-08T18:45:00Z"), 98, localFavorites.contains(38)),
            Event(39, "Hasan Minhaj", EventType.STAND_UP, 44.00, Instant.parse("2024-04-09T21:00:00Z"), 765, localFavorites.contains(39)),
            Event(40, "Open Mic Night", EventType.STAND_UP, 5.00, Instant.parse("2024-04-10T19:30:00Z"), 67, localFavorites.contains(40)),

            // PLAY
            Event(41, "Hamlet", EventType.PLAY, 35.00, Instant.parse("2024-05-01T19:00:00Z"), 432, localFavorites.contains(41)),
            Event(42, "The Lion King", EventType.PLAY, 65.00, Instant.parse("2024-05-02T18:30:00Z"), 1876, localFavorites.contains(42)),
            Event(43, "Wicked", EventType.PLAY, 72.50, Instant.parse("2024-05-03T19:30:00Z"), 1543, localFavorites.contains(43)),
            Event(44, "A Streetcar Named Desire", EventType.PLAY, 28.00, Instant.parse("2024-05-04T20:00:00Z"), 234, localFavorites.contains(44)),
            Event(45, "Les Misérables", EventType.PLAY, 58.75, Instant.parse("2024-05-05T18:00:00Z"), 987, localFavorites.contains(45)),
            Event(46, "The Phantom of the Opera", EventType.PLAY, 69.00, Instant.parse("2024-05-06T19:15:00Z"), 1234, localFavorites.contains(46)),
            Event(47, "Death of a Salesman", EventType.PLAY, 32.50, Instant.parse("2024-05-07T20:30:00Z"), 189, localFavorites.contains(47)),
            Event(48, "Mamma Mia!", EventType.PLAY, 52.00, Instant.parse("2024-05-08T18:45:00Z"), 765, localFavorites.contains(48)),
            Event(49, "The Crucible", EventType.PLAY, 26.25, Instant.parse("2024-05-09T19:00:00Z"), 321, localFavorites.contains(49)),
            Event(50, "Chicago the Musical", EventType.PLAY, 61.00, Instant.parse("2024-05-10T20:00:00Z"), 876, localFavorites.contains(49))
        ).toMutableList()
        val DEFAULT_DELAY = 500.milliseconds
    }

    fun orderEvents(events: List<Event>, order: OrderType, descending: Boolean): List<Event> {
        return when (order) {
            OrderType.DATETIME ->
                if (descending) events.sortedByDescending { it.datetime }
                else events.sortedBy { it.datetime }

            OrderType.POPULARITY ->
                if (descending) events.sortedByDescending { it.timesFavorited }
                else events.sortedBy { it.timesFavorited }

            OrderType.PRICE ->
                if (descending) events.sortedByDescending { it.price }
                else events.sortedBy { it.price }
        }
    }

    fun filterEvents(events: List<Event>, filter: EventFilter, isFavorite: Boolean? = null): List<Event> {
        val orderedEvents = orderEvents(events, filter.orderBy, filter.isOrderDescending)
        val filteredEvents = emptyList<Event>().toMutableList()
        for (event in orderedEvents) {
            if (filter.nameFilter != null && !event.name.contains(filter.nameFilter))
                continue
            if (filter.typeFilter != null && !filter.typeFilter.contains(event.type))
                continue
            if (filter.fromPrice != null && event.price < filter.fromPrice)
                continue
            if (filter.toPrice != null && event.price > filter.toPrice)
                continue
            if (filter.fromDateTime != null && event.datetime < filter.fromDateTime)
                continue
            if (filter.toDateTime != null && event.datetime > filter.toDateTime)
                continue
            if (isFavorite != null && localFavorites.contains(event.id) != isFavorite)
                continue
            filteredEvents.add(event)
        }

        return filteredEvents
    }

    fun selectEvents(events: List<Event>, fromId: Int, count: Int): List<Event> {
        val selectedEvents = emptyList<Event>().toMutableList()
        var i = events.indexOfFirst { it.id == fromId } + 1
        while (i < events.size && selectedEvents.size < 10) {
            selectedEvents.add(events[i])
            i += 1
        }

        return selectedEvents
    }

    override suspend fun getEventById(id: Int): Event? {
        delay(DEFAULT_DELAY)
        return eventDataBase.find { it.id == id }
    }

    override suspend fun getTrendingEvents(count: Int): List<Event> {
        delay(DEFAULT_DELAY)
        return selectEvents(
            filterEvents(eventDataBase, EventFilter(orderBy = OrderType.POPULARITY)
            ), 0, count)
    }

    override suspend fun getTrendingEventsFrom(fromId: Int, count: Int): List<Event> {
        delay(DEFAULT_DELAY)
        return selectEvents(
            filterEvents(
                eventDataBase,
                EventFilter(orderBy = OrderType.POPULARITY)
            ), fromId, count
        )
    }

    override suspend fun getFavoriteEvents(count: Int): List<Event> {
        delay(DEFAULT_DELAY)
        return selectEvents(
            filterEvents(
                eventDataBase,
                EventFilter(orderBy = OrderType.POPULARITY),
                true
            ), 0, count
        )
    }

    override suspend fun getFavoriteEventsFrom(fromId: Int, count: Int): List<Event> {
        delay(DEFAULT_DELAY)
        return selectEvents(
            filterEvents(
                eventDataBase,
                EventFilter(orderBy = OrderType.POPULARITY),
                true
            ), fromId, count
        )
    }

    override suspend fun getFilteredEvents(filter: EventFilter, count: Int): List<Event> {
        delay(DEFAULT_DELAY)
        return selectEvents(
            filterEvents(
                eventDataBase,
                filter
            ), 0, count
        )
    }

    override suspend fun getFilteredEventsFrom(filter: EventFilter, fromId: Int, count: Int): List<Event> {
        delay(DEFAULT_DELAY)
        return selectEvents(
            filterEvents(
                eventDataBase,
                filter
            ), fromId, count
        )
    }

    override suspend fun isFavoriteEvent(id: Int): Boolean {
        delay(DEFAULT_DELAY)
        return localFavorites.contains(id)
    }

    override suspend fun setFavoriteEvent(eventId: Int, isFavorite: Boolean): Boolean {
        delay(DEFAULT_DELAY)
        if (isFavorite)
            if (!isFavoriteEvent(eventId))
                localFavorites.add(eventId)
        else
            localFavorites.remove(eventId)

        return true
    }

    override suspend fun updateFavoriteEvents(): Boolean {
        delay(DEFAULT_DELAY)
        return true
    }
}