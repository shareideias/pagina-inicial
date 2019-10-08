package com.shareinstituto

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.shareinstituto.controller.security.ShareAccessManager
import com.shareinstituto.model.dao.MainDao
import com.shareinstituto.model.dao.MemoryMainDao
import io.javalin.Javalin
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    val kodein = Kodein {
        bind<MainDao>() with singleton { MemoryMainDao() }
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

    app.routes(Endpoints(kodein))
    app.start(port)
}