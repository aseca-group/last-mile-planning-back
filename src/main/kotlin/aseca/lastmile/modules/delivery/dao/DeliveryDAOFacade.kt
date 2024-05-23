package aseca.lastmile.modules.delivery.dao

import aseca.lastmile.modules.delivery.model.CreateDeliveryDTO
import aseca.lastmile.modules.delivery.model.Delivery
import aseca.lastmile.modules.delivery.model.Status

interface DeliveryDAOFacade {
    suspend fun createDelivery(deliveryDTO: CreateDeliveryDTO): Delivery?
    suspend fun getDelivery(id: Int): Delivery?
    suspend fun getAllDeliveries(): List<Delivery>
    suspend fun deleteDelivery(id: Int): Boolean
    suspend fun getDeliveriesByDriverId(driverId: Int): List<Delivery>
    suspend fun updateDeliveryStatus(id: Int, status: Status): Boolean
}