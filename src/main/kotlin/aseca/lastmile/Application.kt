package aseca.lastmile

import aseca.lastmile.db.DatabaseSingleton
import aseca.lastmile.modules.delivery.controller.delivery
import aseca.lastmile.modules.driver.controller.driver
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init()
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("Welcome to Last Mile Planning Application")
        }
        driver()
        delivery()
    }
}