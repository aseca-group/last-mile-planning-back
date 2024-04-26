package com.example.model

import kotlinx.serialization.Serializable

@Serializable
class Driver(val id: Int, val name: String, val deliveryPlan: DeliveryPlan? = null) {
    fun startPlan(): Driver {
        return Driver(id, name, deliveryPlan?.start())
    }

    fun currentDelivery(): Delivery? {
        return deliveryPlan?.currentDelivery()
    }

    fun updateCurrentDeliveryStatus(status: Status): Driver {
        return Driver(id, name, deliveryPlan?.updateCurrentDeliveryStatus(status))
    }

    fun hasRoute(): Boolean {
        return deliveryPlan != null
    }

}