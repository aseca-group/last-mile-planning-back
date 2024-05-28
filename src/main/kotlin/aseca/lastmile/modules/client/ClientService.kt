package aseca.lastmile.modules.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

class HttpClientService {
    private val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                    },
                )
            }
        }

    @Serializable
    data class DeliveryIdWrapper(val deliveryId: Int)

    suspend fun removeReservedStock(deliveryId: Int) {
        val url = "http://control-tower-control-tower-1:8080/inventory/removeReservedStock"
        val response: HttpResponse =
            client.patch(url) {
                contentType(ContentType.Application.Json)
                setBody(DeliveryIdWrapper(deliveryId))
            }
        if (!response.status.isSuccess()) {
            throw IOException("Failed to remove reserved stock: ${response.status}")
        }
    }

}