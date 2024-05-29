package aseca.lastmile

import aseca.lastmile.db.DatabaseSingleton
import aseca.lastmile.modules.delivery.controller.delivery
import aseca.lastmile.modules.driver.controller.driver
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.HttpMethod


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init()
    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        anyHost() 
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader("Content-Type")
    }

    routing {
        get("/") {
            call.respondText("Welcome to Last Mile Planning Application")
        }
        driver()
        delivery()
    }
}
