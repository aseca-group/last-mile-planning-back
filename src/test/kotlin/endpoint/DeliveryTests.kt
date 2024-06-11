package endpoint

import aseca.lastmile.modules.client.OrderDTO
import aseca.lastmile.modules.delivery.dao.deliveryDao
import aseca.lastmile.modules.delivery.model.*
import aseca.lastmile.modules.driver.model.*
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import junit.framework.TestCase
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import kotlin.test.*

class DeliveryEndpointTest {


    @Before
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.drop(Deliveries, Drivers)
            SchemaUtils.create(Deliveries, Drivers)
        }
    }

    private fun ApplicationTestBuilder.httpClient(): HttpClient {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        return client
    }

    @Test
    fun testPostDelivery() = testApplication {
        val client = httpClient()

        // Create a driver to assign to the delivery
        postDriver(client, "Pipo gorosito")

        // Create the delivery
        val response = client.post("/delivery") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(1))
        }

        val deliveryId = Json.decodeFromString<Int>(response.bodyAsText())
        val delivery = deliveryDao.getDelivery(deliveryId)

        TestCase.assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(delivery)
        assertEquals(Status.PENDING, delivery.status)
    }


    @Test
    fun testGetDelivery() = testApplication {
        val client = httpClient()

        postDriver(client, "Pipo gorosito")
        client.post("/delivery") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(1))
        }

        val response = client.get("/delivery/1")
        val delivery = Json.decodeFromString<Delivery>(response.bodyAsText())


        TestCase.assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(delivery)
        assertEquals(1, delivery.id)

    }

    @Test
    fun testGetDeliveriesByDriver() = testApplication {
        val client = httpClient()

        postDriver(client, "Pipo gorosito")
        client.post("/delivery") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(1))
        }

        val response = client.get("/delivery/driver/1")
        val deliveries = Json.decodeFromString<List<Delivery>>(response.bodyAsText())

        TestCase.assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(deliveries)
        assertEquals(1, deliveries.size)
    }

    @Test
    fun testGetAllDeliveries() = testApplication {
        val client = httpClient()

        postDriver(client, "Pipo gorosito")
        client.post("/delivery") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(1))
        }

        val response = client.get("/delivery")
        val deliveries = Json.decodeFromString<List<Delivery>>(response.bodyAsText())

        TestCase.assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(deliveries)
        assertEquals(1, deliveries.size)
    }


    @Test
    fun testDeleteDelivery() = testApplication {
        val client = httpClient()

        postDriver(client, "Pipo gorosito")
        client.post("/delivery") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(1))
        }

        val response = client.delete("/delivery/1")

        TestCase.assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Delivery 1 deleted", response.bodyAsText())
    }

    @Test
    fun testUpdateDeliveryStatus() = testApplication {
        val client = httpClient()

        postDriver(client, "Pipo gorosito")
        client.post("/delivery") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(1))
        }

        val response = client.put("/delivery/1") {
            contentType(ContentType.Application.Json)
            setBody(Delivery(1, "A date", Status.IN_PROGRESS, 1, 1))
        }
        val delivery = deliveryDao.getDelivery(1)


        TestCase.assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Delivery 1 updated", response.bodyAsText())
        assertNotNull(delivery)
        assertEquals(Status.IN_PROGRESS, delivery.status)
    }

    @Test
    fun testAssignDeliveryToDriverWithFewestDeliveries() = testApplication {
        val client = httpClient()

        // Create two drivers
        val driver1 = postDriver(client, "Driver 1")
        val driver2 = postDriver(client, "Driver 2")

        // Create a delivery for driver 1 to ensure it has more deliveries
        deliveryDao.createDelivery(CreateDeliveryDTO(Status.PENDING, 1, driver1.id))

        // Create a new delivery
        val response = client.post("/delivery") {
            contentType(ContentType.Application.Json)
            setBody(OrderDTO(2))
        }

        val deliveryId = Json.decodeFromString<Int>(response.bodyAsText())
        val delivery = deliveryDao.getDelivery(deliveryId)

        TestCase.assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(delivery)
        assertEquals(driver2.id, delivery.driverId)
    }

    private suspend fun postDriver(client: HttpClient, name: String): Driver {
        val response = client.post("/driver") {
            contentType(ContentType.Application.Json)
            setBody(CreateDriverDTO(name))
        }
        return Json.decodeFromString(response.bodyAsText())
    }

}