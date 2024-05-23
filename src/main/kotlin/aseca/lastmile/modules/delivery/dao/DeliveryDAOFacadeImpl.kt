package aseca.lastmile.modules.delivery.dao

import aseca.lastmile.db.DatabaseSingleton.dbQuery
import aseca.lastmile.modules.delivery.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class DeliveryDAOFacadeImpl : DeliveryDAOFacade {
    override suspend fun createDelivery(deliveryDTO: CreateDeliveryDTO): Delivery? = dbQuery {
        val insertStatement = Deliveries.insert {
            it[date] = LocalDateTime.now().toString()
            it[status] = deliveryDTO.status
            it[addressId] = deliveryDTO.addressId
            it[driverId] = deliveryDTO.driverId

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
            addressId = row[Deliveries.addressId],
            driverId = row[Deliveries.driverId]
        )
    }
}

val deliveryDao: DeliveryDAOFacade = DeliveryDAOFacadeImpl()