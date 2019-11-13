package com.shareinstituto.controller

import com.shareinstituto.controller.security.ContentType
import com.shareinstituto.controller.security.NotFoundException
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
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PublicController(override val kodein: Kodein) : EndpointGroup, KodeinAware {
    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get(::index)
        get("n/:noticia", ::noticia)
        get("p/:p1", ::pagina)
        get("p/:p1/:p2", ::pagina)
        get("p/:p1/:p2/:p3", ::pagina)
        get("p/:p1/:p2/:p3/:p4", ::pagina)
        get("p/:p1/:p2/:p3/:p4/:p5", ::pagina)
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
        val id = ctx.pathParam("noticia").toIntOrNull() ?: throw NotFoundException(false, ContentType.NOTICIA)

        val noticia = dao.getNoticia(id) ?: throw NotFoundException(false, ContentType.NOTICIA)

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
        val linkPagina = ctx.pathParamMap().asSequence().sortedBy { it.key }.joinToString("/") { it.value }
        val pagina = dao.getPagina(linkPagina) ?: throw NotFoundException(false, ContentType.PAGINA)
        PaginaView(PaginaViewModel(dao.allLinks(), pagina)).render(ctx)
    }
}