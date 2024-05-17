package com.lastmile.modules.driver.controller

import com.lastmile.modules.driver.dao.driverDao
import com.lastmile.modules.driver.model.Driver
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.driver() {
    route("/driver") {
        get {
            call.respond(driverDao.getAllDrivers())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID")
            } else {
                val driver = driverDao.getDriver(id)
                if (driver == null) {
                    call.respondText("Driver not found")
                } else {
                    call.respond(driver)
                }
            }
        }
        post {
            val driver = call.receive<Driver>()
            val createdDriver = driverDao.createDriver(driver.name)
            if (createdDriver == null) {
                call.respondText("Failed to create driver")
            } else {
                call.respond(createdDriver)
            }
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID")
            } else {
                val deleted = driverDao.deleteDriver(id)
                if (deleted) {
                    call.respondText("Driver $id deleted")
                } else {
                    call.respondText("Driver not found")
                }
            }
        }
    }
}