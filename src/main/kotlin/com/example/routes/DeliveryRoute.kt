package com.example.routes


import io.ktor.server.routing.*

fun Route.deliveryRouting() {
    route("/deliveries") {
        get {
            // return all deliveries
            // send request to control tower
        }
        get("/{id}") {
            // return a delivery by id
            // send request to control tower
        }
        post {
            // create a new delivery
            // send request to control tower
        }
        put("/{id}") {
            // update a delivery by id
            // send request to control tower
        }
    }
}