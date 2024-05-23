package aseca.lastmile.modules.delivery.dao

import aseca.lastmile.modules.delivery.model.CreateDeliveryDTO
import aseca.lastmile.modules.delivery.model.Delivery
import aseca.lastmile.modules.delivery.model.DeliveryDTO
import aseca.lastmile.modules.delivery.model.Status
import java.time.LocalDateTime

interface DeliveryDAOFacade {
    suspend fun createDelivery(deliveryDTO: CreateDeliveryDTO): Delivery?
    suspend fun getDelivery(id: Int): DeliveryDTO?
    suspend fun getAllDeliveries(): List<DeliveryDTO>
    suspend fun deleteDelivery(id: Int): Boolean
    suspend fun getDeliveriesByDriverId(driverId: Int): List<DeliveryDTO>
    suspend fun updateDeliveryStatus(id: Int, status: Status): Boolean
}