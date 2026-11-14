package com.app.lets_go_app.model

import androidx.compose.runtime.mutableStateListOf
import java.time.Instant

class EventProvider {

    companion object {
        val allEvents = mutableStateListOf<Event>(
            Event(
                0,
                "Let's rock!",
                EventType.CONCERT,
                4599.0,
                Instant.parse("2026-11-11T21:00:00Z"),
                66
            ),
            Event(
                1,
                "Shape of you",
                EventType.MOVIE,
                315.0,
                Instant.parse("2026-12-08T17:20:00Z"),
                91
            ),
            Event(
                2,
                "Ancient Egypt",
                EventType.EXHIBITION,
                250.0,
                Instant.parse("2026-12-16T09:00:00Z"),
                76
            ),
            Event(
                3,
                "Comedy Club",
                EventType.STAND_UP,
                1625.0,
                Instant.parse("2027-01-25T21:30:00Z"),
                57
            ),
            Event(
                4,
                "Romeo & Juliet",
                EventType.PLAY,
                1700.0,
                Instant.parse("2027-01-27T16:30:00Z"),
                112
            )
        )

        public fun getEventById(id: Int): Event? {
            return allEvents.firstOrNull {e ->
                e.id == id
            }
        }
    }
}