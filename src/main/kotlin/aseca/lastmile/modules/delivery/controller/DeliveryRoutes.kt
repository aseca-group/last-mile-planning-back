package aseca.lastmile.modules.delivery.controller

import aseca.lastmile.modules.client.HttpClientService
import aseca.lastmile.modules.client.OrderDTO
import aseca.lastmile.modules.delivery.dao.deliveryDao
import aseca.lastmile.modules.delivery.model.CreateDeliveryDTO
import aseca.lastmile.modules.delivery.model.Delivery
import aseca.lastmile.modules.delivery.model.Status
import aseca.lastmile.modules.driver.dao.driverDao
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

val clientService = HttpClientService()

fun Route.delivery() {
    route("/delivery") {
        get {
            call.respond(deliveryDao.getAllDeliveries())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID")
            } else {
                val delivery = deliveryDao.getDelivery(id)
                if (delivery == null) {
                    call.respondText("Delivery not found")
                } else {
                    call.respond(delivery)
                }
            }
        }
        post {
            val orderDTO = call.receive<OrderDTO>()
            // Get all existing drivers from the database
            val allDrivers = driverDao.getAllDrivers()

            if (allDrivers.isEmpty()) {
                call.respondText("Error: No drivers available to assign the delivery.")
            } else {
                // Choose a random driver ID from the list of existing drivers
                val randomDriverId = allDrivers.random().id

                val createdDelivery = deliveryDao.createDelivery(CreateDeliveryDTO(Status.PENDING, orderDTO.addressId, randomDriverId))
                if (createdDelivery == null) {
                    call.respondText("Failed to create delivery")
                } else {
                    call.respond(createdDelivery.id)
                }
            }
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID")
            } else {
                val deleted = deliveryDao.deleteDelivery(id)
                if (deleted) {
                    call.respondText("Delivery $id deleted")
                } else {
                    call.respondText("Delivery not found")
                }
            }
        }
        get("/driver/{driverId}") {
            val driverId = call.parameters["driverId"]?.toIntOrNull()
            if (driverId == null) {
                call.respondText("Invalid ID")
            } else {
                call.respond(deliveryDao.getDeliveriesByDriverId(driverId))
            }
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID")
            } else {
                val delivery = call.receive<Delivery>()
                if(delivery.status == Status.COMPLETED) {
                    // remove stock from control tower
                    runBlocking { clientService.removeStock(delivery.id) }
                }
                val updated = deliveryDao.updateDeliveryStatus(id, delivery.status)
                if (updated) {
                    call.respondText("Delivery $id updated")
                } else {
                    call.respondText("Delivery not found")
                }
            }
        }
    }
}