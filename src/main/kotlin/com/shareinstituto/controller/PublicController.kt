package com.shareinstituto.controller

import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.model.page.IndexViewModel
import com.shareinstituto.model.page.NoticiaViewModel
import com.shareinstituto.model.page.PaginaViewModel
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
        get("p/:pagina", ::pagina)
    }

    fun index(ctx: Context) {
        val p = ctx.queryParam("p")?.toIntOrNull()?.coerceAtLeast(0) ?: 0

        val cards = dao.paginateNoticias(0).take(3)
        val noticias = dao.paginateNoticias(p)
        val pessoas = noticias.flatMap { listOfNotNull(it.criadoPorPessoa, it.ultimaModificacaoPorPessoa) }.toSet()
            .mapNotNull { dao.getPessoa(it)?.let { p -> it to p } }.toMap()

        IndexView(IndexViewModel(dao.allLinks(), p, cards, noticias, pessoas)).render(ctx)
    }

    fun noticia(ctx: Context) {
        val id = ctx.pathParam("noticia").toInt()

        val noticia = dao.getNoticia(id) ?: throw NotFoundResponse()

        NoticiaView(
            NoticiaViewModel(
                dao.allLinks(),
                noticia,
                dao.getPessoa(noticia.criadoPorPessoa),
                noticia.ultimaModificacaoPorPessoa?.let(dao::getPessoa)
            )
        ).render(ctx)
    }

    fun pagina(ctx: Context) {
        val linkPagina = ctx.pathParam("pagina")
        PaginaView(PaginaViewModel(dao.allLinks(), dao.getPagina(linkPagina) ?: throw NotFoundResponse())).render(ctx)
    }
}