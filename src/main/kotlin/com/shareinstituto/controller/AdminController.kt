package com.shareinstituto.controller

import com.shareinstituto.controller.security.MainRole.ADMIN
import com.shareinstituto.model.dao.MainDao
import com.shareinstituto.model.dao.MemoryMainDao
import com.shareinstituto.view.AdminView
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.EndpointGroup
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.Context
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class AdminController(override val kodein: Kodein) : EndpointGroup, KodeinAware {
    val dao: MainDao = MemoryMainDao()

    override fun addEndpoints() {
        get(::admin, roles(ADMIN))
    }

    fun admin(ctx: Context) {
        AdminView(dao).render(ctx)
    }
}