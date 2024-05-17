package com.lastmile

import com.lastmile.db.DatabaseSingleton
import com.lastmile.modules.delivery.controller.delivery
import com.lastmile.modules.driver.controller.driver
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init()
    install(ContentNegotiation)
    install(Routing) {
        driver()
        delivery()
    }
}
