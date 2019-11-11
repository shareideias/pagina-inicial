package com.shareinstituto.controller.security

import com.shareinstituto.controller.security.MainRole.*
import com.shareinstituto.model.Usuario
import io.javalin.core.security.AccessManager
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.ForbiddenResponse
import io.javalin.http.Handler
import kotlin.text.Charsets.UTF_8
import java.net.URLEncoder.encode as urlEncode

class ShareAccessManager : AccessManager {
    override fun manage(handler: Handler, ctx: Context, permittedRoles: MutableSet<Role>) {
        if (permittedRoles.isEmpty()) {
            handler.handle(ctx)
        } else {
            when (getRole(ctx.sessionAttribute<Usuario>("USER"))) {
                in permittedRoles -> handler.handle(ctx)
                ANYONE -> {
                    val thenUrl = listOfNotNull(ctx.path(), ctx.queryString()).joinToString("?")
                    ctx.redirect("/login?err=unauthorized&then=${urlEncode(thenUrl, UTF_8)}")
                }
                else -> throw ForbiddenResponse()
            }
        }
    }

    companion object {
        fun getRole(usuario: Usuario?): MainRole {
            return when (usuario?.admin) {
                true -> SUPERADMIN
                false -> ADMIN
                else -> ANYONE
            }
        }
    }
}