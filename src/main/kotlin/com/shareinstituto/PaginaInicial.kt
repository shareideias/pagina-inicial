package com.shareinstituto

import com.shareinstituto.controller.MainController
import io.javalin.Javalin

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    val app = Javalin.create { cfg ->
        cfg.addStaticFiles("public")
    }

    app.routes(MainController())
    app.start(port)
}