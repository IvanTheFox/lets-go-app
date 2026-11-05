package com.example.lab2.model

import java.time.Instant

enum class EventType(val typeName: String) {
    MOVIE("Movie"),
    CONCERT("Concert"),
    EXHIBISTION("Exhibition"),
    STAND_UP("Stand-up"),
    PLAY("Play")
}

data class Event(
    val id: Int,
    val name: String,
    val type: EventType,
    val price: Double,
    val datetime: Instant,
    val timesFavorited: Int
)