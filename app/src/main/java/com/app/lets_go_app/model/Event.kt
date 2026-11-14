package com.app.lets_go_app.model

import com.app.lets_go_app.R
import java.time.Instant

enum class EventType(val typeNameId: Int) {
    MOVIE(R.string.event_type_movie),
    CONCERT(R.string.event_type_concert),
    EXHIBITION(R.string.event_type_exhibition),
    STAND_UP(R.string.event_type_stand_up),
    PLAY(R.string.event_type_play)
}

data class Event(
    val id: Int,
    val name: String,
    val type: EventType,
    val price: Double,
    val datetime: Instant,
    val timesFavorited: Int
)