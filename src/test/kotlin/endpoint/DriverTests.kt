package endpoint

import aseca.lastmile.modules.delivery.model.Deliveries
import aseca.lastmile.modules.driver.model.CreateDriverDTO
import aseca.lastmile.modules.driver.model.Driver
import aseca.lastmile.modules.driver.model.Drivers
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import kotlin.test.*

class DriverEndpointTest {

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
    fun `test creating a driver`() = testApplication {
        val client = httpClient()

        val response = client.post("/driver") {
            contentType(ContentType.Application.Json)
            setBody(CreateDriverDTO("Pipo gorosito"))
        }

        val driver = Json.decodeFromString<Driver>(response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Pipo gorosito", driver.name)
    }

    @Test
    fun `test getting a driver`() = testApplication {
        val client = httpClient()

        val response = client.post("/driver") {
            contentType(ContentType.Application.Json)
            setBody(CreateDriverDTO("Pipo gorosito"))
        }

        val driver = Json.decodeFromString<Driver>(response.bodyAsText())

        val getResponse = client.get("/driver/${driver.id}")

        val getDriver = Json.decodeFromString<Driver>(getResponse.bodyAsText())

        assertEquals(HttpStatusCode.OK, getResponse.status)
        assertEquals("Pipo gorosito", getDriver.name)
    }

    @Test
    fun `test getting all drivers`() = testApplication {
        val client = httpClient()

        val response = client.get("/driver")

        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response.bodyAsText())
    }

    @Test
    fun `test deleting a driver`() = testApplication {
        val client = httpClient()

        val response = client.post("/driver") {
            contentType(ContentType.Application.Json)
            setBody(CreateDriverDTO("Pipo gorosito"))
        }

        val driver = Json.decodeFromString<Driver>(response.bodyAsText())

        val deleteResponse = client.delete("/driver/${driver.id}")

        assertEquals(HttpStatusCode.OK, deleteResponse.status)
        assertEquals("Driver ${driver.id} deleted", deleteResponse.bodyAsText())
    }
}
