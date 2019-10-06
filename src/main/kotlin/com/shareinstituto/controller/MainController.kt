package com.shareinstituto.controller

import com.shareinstituto.model.dao.MainDao
import com.shareinstituto.model.dao.MemoryMainDao
import com.shareinstituto.view.AdminView
import com.shareinstituto.view.IndexView
import com.shareinstituto.view.LoginView
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context

class MainController : EndpointGroup {
    val dao: MainDao = MemoryMainDao()

    override fun addEndpoints() {
        get(::index)
        get("administracao", ::administracao)
        get("login", ::login)

        //get("p/:page", ::page)
    }

    fun index(ctx: Context) {
        val p = ctx.queryParam("p")?.toIntOrNull()?.coerceAtLeast(0) ?: 0
        IndexView(p, dao).render(ctx)
    }

    fun administracao(ctx: Context) {
        AdminView(dao).render(ctx)
    }

    fun login(ctx: Context) {
        LoginView(dao).render(ctx)
    }
    //fun page(ctx: Context) {
    //
    //}
}