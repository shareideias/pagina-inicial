package com.shareinstituto.controller

import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.IndexView
import com.shareinstituto.view.NoticiaView
import com.shareinstituto.view.PaginaView
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PublicController(override val kodein: Kodein) : EndpointGroup, KodeinAware {
    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get(::index)
        get("n/:noticia", ::noticia)
        get("p/:page", ::page)
    }

    fun index(ctx: Context) {
        val p = ctx.queryParam("p")?.toIntOrNull()?.coerceAtLeast(0) ?: 0
        IndexView(p, dao).render(ctx)
    }

    fun noticia(ctx: Context) {
        val id = ctx.pathParam("noticia").toInt()
        NoticiaView(dao, dao.getNoticia(id) ?: throw NotFoundResponse()).render(ctx)
    }

    fun page(ctx: Context) {
        val linkPagina = ctx.pathParam("page")
        PaginaView(dao, dao.getPagina(linkPagina) ?: throw NotFoundResponse()).render(ctx)
    }
}