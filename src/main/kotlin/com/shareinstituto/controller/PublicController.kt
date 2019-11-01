package com.shareinstituto.controller

import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.IndexView
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PublicController(override val kodein: Kodein) : EndpointGroup, KodeinAware {
    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get(::index)
        //get("p/:page", ::page)
    }

    fun index(ctx: Context) {
        val p = ctx.queryParam("p")?.toIntOrNull()?.coerceAtLeast(0) ?: 0
        IndexView(p, dao).render(ctx)
    }

    //fun page(ctx: Context) {
    //
    //}
}