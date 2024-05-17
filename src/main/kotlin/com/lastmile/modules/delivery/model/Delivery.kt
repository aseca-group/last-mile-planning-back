package com.lastmile.modules.delivery.model

import com.lastmile.modules.driver.model.Drivers
import com.lastmile.modules.driver.model.Status
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class Delivery(val id: Int, val date: LocalDateTime, val status: Status, val driverId: Int)


object Deliveries: IntIdTable() {
    val date = datetime("date")
    val status = enumeration("status", Status::class)
    val driverId = integer("driverId").references(Drivers.id)
}
