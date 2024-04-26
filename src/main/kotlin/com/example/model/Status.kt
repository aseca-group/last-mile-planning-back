package com.example.model

import kotlinx.serialization.Serializable

@Serializable
enum class Status {
    PENDING,
    DELIVERED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    FAILED,
}