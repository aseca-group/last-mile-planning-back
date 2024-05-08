package com.lastmile.routes

import com.lastmile.models.Schedule
import com.lastmile.requests.createSchedule
import com.lastmile.requests.getAllSchedules
import com.lastmile.requests.getScheduleById
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.scheduleRouting() {
    route("/schedule") {
        get {
            // return schedules
            try {
                val schedules = getAllSchedules()
                call.respond(schedules)
            } catch (e: Exception) {
                call.respondText("Failed to retrieve schedules", status = HttpStatusCode.InternalServerError)
            }
        }
        get("/{id}") {
            // return a schedule by id
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText("Missing or malformed id", status = HttpStatusCode.BadRequest)
            try {
                val schedule = getScheduleById(id)
                if (schedule != null) {
                    call.respond(schedule)
                } else {
                    call.respondText("No schedule found with id $id", status = HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respondText("Failed to retrieve schedule", status = HttpStatusCode.InternalServerError)
            }
        }
        post {
            // create a new schedule
            val schedule = call.receive<Schedule>()
            try {
                createSchedule(schedule)
                call.respond(HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondText("Failed to create schedule", status = HttpStatusCode.InternalServerError)
            }
        }
        put("/{id}") {
            // update a delivery plan by id
            val id = call.parameters["id"]?.toInt() ?: return@put call.respondText("Missing or malformed id", status = HttpStatusCode.BadRequest)
            val schedule = call.receive<Schedule>()
            if (schedule.id != id) {
                return@put call.respondText("Mismatched id", status = HttpStatusCode.BadRequest)
            }
            try {
                createSchedule(schedule)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respondText("Failed to update schedule", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}