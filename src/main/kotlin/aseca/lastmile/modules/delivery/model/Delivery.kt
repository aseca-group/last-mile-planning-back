package aseca.lastmile.modules.delivery.model

import aseca.lastmile.modules.driver.model.Drivers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

@Serializable
data class Delivery(
    val id: Int,
    @Contextual val date: LocalDateTime,
    val status: Status,
    val addressId: Int,
    val driverId: Int)


object Deliveries: IntIdTable() {
    val date: Column<LocalDateTime> = datetime("date").defaultExpression(CurrentDateTime)
    val status = enumeration("status", Status::class)
    val addressId = integer("addressId")
    val driverId = integer("driverId").references(Drivers.id)
}