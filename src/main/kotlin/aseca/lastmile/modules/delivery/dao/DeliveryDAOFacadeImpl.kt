package aseca.lastmile.modules.delivery.dao

import aseca.lastmile.db.DatabaseSingleton.dbQuery
import aseca.lastmile.modules.delivery.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class DeliveryDAOFacadeImpl : DeliveryDAOFacade {
    override suspend fun createDelivery(deliveryDTO: CreateDeliveryDTO): Delivery? = dbQuery {
        val insertStatement = Deliveries.insert {
            it[Deliveries.date] = LocalDateTime.now()
            it[Deliveries.status] = deliveryDTO.status
            it[Deliveries.addressId] = deliveryDTO.addressId
            it[Deliveries.driverId] = deliveryDTO.driverId

        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToDelivery)
    }

    override suspend fun getDelivery(id: Int): DeliveryDTO? = dbQuery {
        Deliveries.select { Deliveries.id eq id }.map(::resultRowToDeliveryDTO).singleOrNull()
    }

    override suspend fun getAllDeliveries(): List<DeliveryDTO> = dbQuery {
        Deliveries.selectAll().map(::resultRowToDeliveryDTO)
    }

    override suspend fun deleteDelivery(id: Int): Boolean = dbQuery {
        Deliveries.deleteWhere { Deliveries.id eq id } > 0
    }

    override suspend fun getDeliveriesByDriverId(driverId: Int): List<DeliveryDTO> = dbQuery {
        Deliveries.select { Deliveries.driverId eq driverId }.map(::resultRowToDeliveryDTO)
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

    private fun resultRowToDeliveryDTO(row: ResultRow): DeliveryDTO {
        return DeliveryDTO(
            id = row[Deliveries.id].value,
            status = row[Deliveries.status],
            addressId = row[Deliveries.addressId],
            driverId = row[Deliveries.driverId]
        )
    }

}

val deliveryDao: DeliveryDAOFacade = DeliveryDAOFacadeImpl()