package com.app.lets_go_app.model

import java.time.Instant

class EventFilter(
    val fromId: Int?,
    val toId: Int?,
    val nameFilter: String?,
    val typeFilter: Set<EventType>?,
    val fromPrice: Double?,
    val toPrice: Double?,
    val fromDateTime: Instant?,
    val toDateTime: Instant?,
    val isOrderDescending: Boolean,
    val orderBy: OrderType
)

enum class OrderType(typeName: String) {
    POPULARITY("popularity"),
    DATETIME("datetime"),
    PRICE("price")
}