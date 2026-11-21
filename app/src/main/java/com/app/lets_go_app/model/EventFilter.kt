package com.app.lets_go_app.model

import java.time.Instant

class EventFilter(
    val nameFilter: String? = null,
    val typeFilter: Set<EventType>? = null,
    val fromPrice: Double? = null,
    val toPrice: Double? = null,
    val fromDateTime: Instant? = null,
    val toDateTime: Instant? = null,
    val isOrderDescending: Boolean = true,
    val orderBy: OrderType = OrderType.POPULARITY
)

enum class OrderType(typeName: String) {
    POPULARITY("popularity"),
    DATETIME("datetime"),
    PRICE("price")
}