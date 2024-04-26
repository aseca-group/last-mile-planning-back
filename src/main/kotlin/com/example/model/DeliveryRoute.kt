package com.example.model

import kotlinx.serialization.Serializable

@Serializable
class DeliveryRoute(val id: Int, val deliveries: List<Delivery>) {


    fun start(): DeliveryRoute {
        // update all deliveries to in transit and the first one to out for delivery
        return DeliveryRoute(id, deliveries.mapIndexed { index, delivery ->
            if (index == 0) {
                Delivery(delivery.id, delivery.address, Status.OUT_FOR_DELIVERY)
            } else {
                Delivery(delivery.id, delivery.address, Status.IN_TRANSIT)
            }
        })
    }

    // return the first delivery with status out for delivery
    fun currentDelivery(): Delivery? {
        return deliveries.firstOrNull { it.status == Status.OUT_FOR_DELIVERY }
    }

    // update the status of the current delivery and change the next delivery status to out for delivery
    fun updateCurrentDeliveryStatus(status: Status): DeliveryRoute {
        val currentDelivery = currentDelivery()
        if (currentDelivery != null) {
            val updatedDeliveries = deliveries.mapIndexed { index, delivery ->
                if (delivery.id == currentDelivery.id) {
                    Delivery(delivery.id, delivery.address, status)
                } else if (index == deliveries.indexOf(currentDelivery) + 1) {
                    Delivery(delivery.id, delivery.address, Status.OUT_FOR_DELIVERY)
                } else {
                    delivery
                }
    }
            return DeliveryRoute(id, updatedDeliveries)
        }
        return this
    }
}