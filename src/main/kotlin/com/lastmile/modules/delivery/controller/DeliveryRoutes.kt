package com.lastmile.modules.delivery.controller

import com.lastmile.modules.delivery.dao.deliveryDao
import com.lastmile.modules.delivery.model.Delivery
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
            val delivery = call.receive<Delivery>()
            val createdDelivery = deliveryDao.createDelivery(delivery.date, delivery.status, delivery.driverId)
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