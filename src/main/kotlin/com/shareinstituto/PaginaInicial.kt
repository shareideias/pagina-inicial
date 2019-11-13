package com.shareinstituto

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.shareinstituto.controller.AdminController
import com.shareinstituto.controller.ErrorHandler
import com.shareinstituto.controller.LoginController
import com.shareinstituto.controller.PublicController
import com.shareinstituto.controller.security.ShareAccessManager
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.model.dao.JdbiDataAccessObject
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import org.kodein.di.Kodein
import org.kodein.di.generic.*

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    val kodein = Kodein {
        bind<ObjectMapper>() with singleton { jacksonObjectMapper() }
        bind<DataAccessObject>() with eagerSingleton { JdbiDataAccessObject("jdbc:postgresql:shareideias") }
        bind<Algorithm>() with provider {
            Algorithm.HMAC256(System.getenv("secret") ?: "shareinstituto_is_very_secret")
        }
        bind<JWTVerifier>() with provider {
            JWT.require(instance())
                .withIssuer("associacaoshare")
                .build()
        }
    }

    val app = Javalin.create { cfg ->
        cfg.accessManager(ShareAccessManager())
        cfg.addStaticFiles("public")
    }

    app.routes {
        PublicController(kodein).addEndpoints()
        LoginController(kodein).addEndpoints()
        path("admin") { AdminController(kodein).addEndpoints() }
    }
    ErrorHandler(kodein).run { app.addErrorHandlers() }
    app.start(port)
}