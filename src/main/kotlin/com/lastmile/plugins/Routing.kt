package com.lastmile.plugins
import com.lastmile.routes.deliveryRouting
import com.lastmile.routes.scheduleRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        deliveryRouting()
        scheduleRouting()
    }
}
