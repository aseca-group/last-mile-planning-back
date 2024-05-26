package aseca.lastmile.modules.delivery.controller

import aseca.lastmile.modules.client.OrderDTO
import aseca.lastmile.modules.delivery.dao.deliveryDao
import aseca.lastmile.modules.delivery.model.CreateDeliveryDTO
import aseca.lastmile.modules.delivery.model.Delivery
import aseca.lastmile.modules.delivery.model.Status
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
            val createdDelivery = deliveryDao.createDelivery(CreateDeliveryDTO(Status.PENDING, orderDTO.addressId, 1))
            if (createdDelivery == null) {
                call.respondText("Failed to create delivery")
            } else {
                call.respond(createdDelivery)
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