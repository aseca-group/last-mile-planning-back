package com.lastmile.modules.delivery.dao

import com.lastmile.modules.delivery.model.Delivery
import com.lastmile.modules.delivery.model.Status
import java.time.LocalDateTime

interface DeliveryDAOFacade {
    suspend fun createDelivery(date: LocalDateTime, status: Status, driverId: Int): Delivery?
    suspend fun getDelivery(id: Int): Delivery?
    suspend fun getAllDeliveries(): List<Delivery>
    suspend fun deleteDelivery(id: Int): Boolean
    suspend fun getDeliveriesByDriverId(driverId: Int): List<Delivery>
    suspend fun updateDeliveryStatus(id: Int, status: Status): Boolean
}