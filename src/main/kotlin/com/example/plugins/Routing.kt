package com.example.plugins

import com.example.routes.deliveryPlanRouting
import com.example.routes.deliveryRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        deliveryRouting()
        deliveryPlanRouting()
    }
}
