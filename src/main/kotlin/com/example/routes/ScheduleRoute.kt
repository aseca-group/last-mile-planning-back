package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deliveryPlanRouting() {
    route("/delivery-plans") {
        get {
            // return all delivery plans
            // send request to control tower
        }
        get("/{id}") {
            // return a delivery plan by id
            // send request to control tower
        }
        post {
            // create a new delivery plan
            // send request to control tower
        }
        put("/{id}") {
            // update a delivery plan by id
            // send request to control tower
        }
    }
}