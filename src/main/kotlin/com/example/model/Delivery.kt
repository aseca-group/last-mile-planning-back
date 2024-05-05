package com.example.model

import kotlinx.serialization.Serializable

@Serializable
class Delivery(val id: Int, val address: String, val status: Status = Status.PENDING) {
}

