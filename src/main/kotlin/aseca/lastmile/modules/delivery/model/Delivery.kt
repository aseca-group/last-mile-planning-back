package aseca.lastmile.modules.delivery.model

import aseca.lastmile.modules.driver.model.Drivers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable


@Serializable
data class Delivery(
    val id: Int,
    val date: String,
    val status: Status,
    val addressId: Int,
    val driverId: Int)


object Deliveries: IntIdTable() {
    val date = varchar("date", 50)
    val status = enumeration("status", Status::class)
    val addressId = integer("addressId")
    val driverId = integer("driverId").references(Drivers.id)
}