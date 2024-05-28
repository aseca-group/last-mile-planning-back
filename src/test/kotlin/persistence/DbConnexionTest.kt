package persistence

import aseca.lastmile.module
import aseca.lastmile.modules.delivery.model.Deliveries
import aseca.lastmile.modules.delivery.model.Status
import aseca.lastmile.modules.driver.model.Drivers
import io.ktor.server.application.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DbConnexionTest {
    @Before
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Drivers)
            SchemaUtils.create(Deliveries)
        }
    }

    @After
    fun teardown() {
        transaction {
            SchemaUtils.drop(Deliveries)
            SchemaUtils.drop(Drivers)
        }
    }

    @Test
    fun testDriverInsertion() = withTestApplication(Application::module){
        transaction {
            val driverId = Drivers.insert {
                it[name] = "Tista"
            }.get(Drivers.id)

            assertNotNull(driverId)

            val insertedDriver = Drivers.select { Drivers.id eq driverId }.singleOrNull()
            assertNotNull(insertedDriver)
            assertEquals("Tista", insertedDriver.get(Drivers.name))
        }
    }


    @Test
    fun testDeliveryInsertion() = withTestApplication(Application::module) {
        transaction {
            val dId = Drivers.insert {
                it[name] = "Tista"
            }.get(Drivers.id).value

            val deliveryId = Deliveries.insert {
                it[status] = Status.PENDING
                it[addressId] = 3
                it[driverId] = dId
                it[date] = "30/05"
            }.get(Deliveries.id)

            assertNotNull(deliveryId)

            val insertedDelivery = Deliveries.select { Deliveries.id eq deliveryId }.singleOrNull()
            assertNotNull(insertedDelivery)
            assertEquals(dId, insertedDelivery.get(Deliveries.driverId))
        }
    }

    @Test
    fun testDatabaseConnectionFailures() {
        transaction {
            Drivers.insert {
                it[name] = "Tistaaaa"
            }
        }

        assertThrows<Exception>() {
            transaction {
                val connection = TransactionManager.current().connection //simulates db down
                connection.close()

                Drivers.insert { //tries to insert entity
                    it[name] = "Another tistaaa"
                }
            }
        }
    }

}