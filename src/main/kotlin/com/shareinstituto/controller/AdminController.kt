package com.shareinstituto.controller

import com.shareinstituto.controller.security.MainRole.ADMIN
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.AdminView
import com.shareinstituto.view.EditarLinkView
import com.shareinstituto.view.EditarNoticiaView
import com.shareinstituto.view.EditarPaginaView
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
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
        post("novaPagina", ::criarNovaPagina, roles(ADMIN))

        get("novaNoticia", ::novaNoticia, roles(ADMIN))
        post("novaNoticia", ::criarNovaNoticia, roles(ADMIN))

        get("novoLink", ::novoLink, roles(ADMIN))
        post("novoLink", ::criarNovoLink, roles(ADMIN))

        get("editarPagina/:pagina", ::editarPagina, roles(ADMIN))
        post("editarPagina/:pagina", ::salvarEdicaoPagina, roles(ADMIN))

        get("editarNoticia/:noticia", ::editarNoticia, roles(ADMIN))
        post("editarNoticia/:noticia", ::salvarEdicaoNoticia, roles(ADMIN))

        get("editarLink/:link", ::editarLink, roles(ADMIN))
        post("editarLink/:link", ::salvarEdicaoLink, roles(ADMIN))

        get("removerPagina/:pagina", ::removerPagina, roles(ADMIN))
        get("removerNoticia/:noticia", ::removerNoticia, roles(ADMIN))
        get("removerLink/:link", ::removerLink, roles(ADMIN))
    }

    fun admin(ctx: Context) {
        AdminView(dao).render(ctx)
    }

    fun novaPagina(ctx: Context) {
        EditarPaginaView(dao).render(ctx)
    }

    fun criarNovaPagina(ctx: Context) {

    }

    fun novaNoticia(ctx: Context) {
        EditarNoticiaView(dao).render(ctx)
    }

    fun criarNovaNoticia(ctx: Context) {

    }

    fun novoLink(ctx: Context) {
        EditarLinkView(dao).render(ctx)
    }

    fun criarNovoLink(ctx: Context) {

    }

    fun editarPagina(ctx: Context) {
        EditarPaginaView(dao).render(ctx)
    }

    fun salvarEdicaoPagina(ctx: Context) {

    }

    fun editarNoticia(ctx: Context) {
        EditarNoticiaView(dao).render(ctx)
    }

    fun salvarEdicaoNoticia(ctx: Context) {

    }

    fun editarLink(ctx: Context) {
        EditarLinkView(dao).render(ctx)
    }

    fun salvarEdicaoLink(ctx: Context) {

    }

    fun removerPagina(ctx: Context) {

    }

    fun removerNoticia(ctx: Context) {

    }

    fun removerLink(ctx: Context) {

    }
}