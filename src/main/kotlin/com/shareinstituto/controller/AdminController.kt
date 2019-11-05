package com.shareinstituto.controller

import com.shareinstituto.controller.security.MainRole.ADMIN
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.AdminView
import com.shareinstituto.view.EditarLinkView
import com.shareinstituto.view.EditarNoticiaView
import com.shareinstituto.view.EditarPaginaView
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.EndpointGroup
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.Context
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class AdminController(override val kodein: Kodein) : EndpointGroup, KodeinAware {
    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get(::admin, roles(ADMIN))
        get("novaPagina", ::novaPagina, roles(ADMIN))
        get("novaNoticia", ::novaNoticia, roles(ADMIN))
        get("editarPagina/:pagina", ::editarPagina, roles(ADMIN))
        get("editarNoticia/:noticia", ::editarNoticia, roles(ADMIN))
        get("novoLink", ::novoLink, roles(ADMIN))
        get("editarLink/:link", ::editarLink, roles(ADMIN))
    }

    fun admin(ctx: Context) {
        AdminView(dao).render(ctx)
    }

    fun novaPagina(ctx: Context) {
        EditarPaginaView(dao).render(ctx)
    }

    fun novaNoticia(ctx: Context) {
        EditarNoticiaView(dao).render(ctx)
    }

    fun editarPagina(ctx: Context) {
        EditarPaginaView(dao).render(ctx)
    }

    fun editarNoticia(ctx: Context) {
        EditarNoticiaView(dao).render(ctx)
    }

    fun novoLink(ctx: Context) {
        EditarLinkView(dao).render(ctx)
    }

    fun editarLink(ctx: Context) {
        EditarLinkView(dao).render(ctx)
    }


}