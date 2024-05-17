package com.lastmile.modules.delivery.dao

import com.lastmile.db.DatabaseSingleton.dbQuery
import com.lastmile.modules.delivery.model.Deliveries
import com.lastmile.modules.delivery.model.Delivery
import com.lastmile.modules.delivery.model.Status
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class DeliveryDAOFacadeImpl : DeliveryDAOFacade {
    override suspend fun createDelivery(date: LocalDateTime, status: Status, driverId: Int): Delivery? = dbQuery {
        val insertStatement = Deliveries.insert {
            it[Deliveries.date] = date
            it[Deliveries.status] = status
            it[Deliveries.driverId] = driverId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToDelivery)
    }

    override suspend fun getDelivery(id: Int): Delivery? = dbQuery {
        Deliveries.select { Deliveries.id eq id }.map(::resultRowToDelivery).singleOrNull()
    }

    override suspend fun getAllDeliveries(): List<Delivery> = dbQuery {
        Deliveries.selectAll().map(::resultRowToDelivery)
    }

    override suspend fun deleteDelivery(id: Int): Boolean = dbQuery {
        Deliveries.deleteWhere { Deliveries.id eq id } > 0
    }

    override suspend fun getDeliveriesByDriverId(driverId: Int): List<Delivery> = dbQuery {
        Deliveries.select { Deliveries.driverId eq driverId }.map(::resultRowToDelivery)
    }

    override suspend fun updateDeliveryStatus(id: Int, status: Status): Boolean = dbQuery {
        Deliveries.update({ Deliveries.id eq id }) {
            it[Deliveries.status] = status
        } > 0
    }

    private fun resultRowToDelivery(row: ResultRow): Delivery {
        return Delivery(
            id = row[Deliveries.id].value,
            date = row[Deliveries.date],
            status = row[Deliveries.status],
            driverId = row[Deliveries.driverId]
        )
    }

}

val deliveryDao: DeliveryDAOFacade = DeliveryDAOFacadeImpl()