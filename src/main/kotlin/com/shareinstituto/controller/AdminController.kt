package com.shareinstituto.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shareinstituto.controller.security.MainRole.ADMIN
import com.shareinstituto.controller.security.MainRole.SUPERADMIN
import com.shareinstituto.model.Pagina
import com.shareinstituto.model.Usuario
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.model.page.AdminViewModel
import com.shareinstituto.model.page.EditarNoticiaViewModel
import com.shareinstituto.model.page.EditarPaginaViewModel
import com.shareinstituto.utils.brazilZone
import com.shareinstituto.view.AdminView
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
import java.net.URLEncoder
import java.time.OffsetDateTime
import kotlin.text.Charsets.UTF_8

class AdminController(override val kodein: Kodein) : EndpointGroup, KodeinAware {
    val dao: DataAccessObject by instance()
    val mapper: ObjectMapper by instance()
    val requiredRole = roles(ADMIN, SUPERADMIN)

    override fun addEndpoints() {
        get(::admin, requiredRole)

        get("novaPagina", ::novaPagina, requiredRole)
        post("novaPagina", ::criarNovaPagina, requiredRole)

        get("novaNoticia", ::novaNoticia, requiredRole)
        post("novaNoticia", ::criarNovaNoticia, requiredRole)

        get("editarPagina/:pagina", ::editarPagina, requiredRole)
        post("editarPagina/:pagina", ::salvarEdicaoPagina, requiredRole)

        get("editarNoticia/:noticia", ::editarNoticia, requiredRole)
        post("editarNoticia/:noticia", ::salvarEdicaoNoticia, requiredRole)

        get("removerPagina/:pagina", ::removerPagina, requiredRole)
        get("removerNoticia/:noticia", ::removerNoticia, requiredRole)

        post("novoLink", ::novoLink, requiredRole)
        get("swapLinks", ::swapLinks, requiredRole)
        get("deleteLink", ::deleteLink, requiredRole)
    }

    fun admin(ctx: Context) {
        val noticias = dao.allNoticias()
        val paginas = dao.allPaginas()
        val pessoas = listOf(
            noticias.flatMap { listOfNotNull(it.criadoPorPessoa, it.ultimaModificacaoPorPessoa) },
            paginas.flatMap { listOfNotNull(it.criadoPorPessoa, it.ultimaModificacaoPorPessoa) }
        ).flatten().toSet().mapNotNull { dao.getPessoa(it)?.let { p -> it to p } }.toMap()

        AdminView(AdminViewModel(noticias, paginas, pessoas, dao.allLinks())).render(ctx)
    }

    fun novaPagina(ctx: Context) {
        EditarPaginaView(EditarPaginaViewModel(mapper, null, false)).render(ctx)
    }

    fun criarNovaPagina(ctx: Context) {
        val title = ctx.formParam("title")
        val linkPagina = ctx.formParam("linkPagina")
        val html = ctx.formParam("html")

        if (title != null && linkPagina != null && html != null) {
            dao.insertPagina(linkPagina, title, html, ctx.sessionAttribute<Usuario>("USER")!!.pessoaId)

            ctx.redirect("/admin?novaPagina=success&linkPagina=${URLEncoder.encode(linkPagina, UTF_8)}")
        }
        ctx.redirect("/admin?novaPagina=invalid")
    }

    fun novaNoticia(ctx: Context) {
        EditarNoticiaView(EditarNoticiaViewModel(mapper, null, false)).render(ctx)
    }

    fun criarNovaNoticia(ctx: Context) {
        val title = ctx.formParam("title")
        val html = ctx.formParam("html")

        if (title != null && html != null) {
            val id = dao.insertNoticia(title, html, ctx.sessionAttribute<Usuario>("USER")!!.pessoaId).id

            ctx.redirect("/admin?novaNoticia=success&id=$id")
            return
        }
        ctx.redirect("/admin?novaNoticia=invalid")
    }

    fun editarPagina(ctx: Context) {
        val linkPagina = ctx.pathParam("pagina")
        val pagina = dao.getPagina(linkPagina)
            ?: throw NoSuchElementException("GET editarPagina: $linkPagina is not valid.")

        EditarPaginaView(EditarPaginaViewModel(mapper, pagina, true)).render(ctx)
    }

    fun salvarEdicaoPagina(ctx: Context) {
        val linkPagina = ctx.pathParam("pagina")
        val pagina = dao.getPagina(linkPagina)
            ?: throw NoSuchElementException("POST editarPagina: $linkPagina is not valid.")

        val title = ctx.formParam("title")
        val novoLinkPagina = ctx.formParam("linkPagina")
        val html = ctx.formParam("html")

        if (title != null && novoLinkPagina != null && html != null) {
            val p: Pagina
            if (novoLinkPagina != novoLinkPagina) {
                dao.removePagina(novoLinkPagina)
                p = dao.insertPagina(novoLinkPagina, title, html, pagina.criadoPorPessoa)
                p.dataCriacao = pagina.dataCriacao
            } else p = pagina

            p.titulo = title
            p.html = html
            p.dataModificacao = OffsetDateTime.now(brazilZone)
            p.ultimaModificacaoPorPessoa = ctx.sessionAttribute<Usuario>("USER")!!.pessoaId
            dao.updatePagina(p)

            ctx.redirect("/admin?editarPagina=success&linkPagina=${URLEncoder.encode(novoLinkPagina, UTF_8)}")
            return
        }
        ctx.redirect("/admin?editarPagina=invalid")
    }

    fun editarNoticia(ctx: Context) {
        val noticiaId = ctx.pathParam("noticia")
        val id = noticiaId.toIntOrNull()
            ?: throw IllegalArgumentException("GET editarNoticia: $noticiaId is not an integer.")
        val noticia = dao.getNoticia(id) ?: throw NoSuchElementException("GET editarNoticia: $id is not valid.")

        EditarNoticiaView(EditarNoticiaViewModel(mapper, noticia, true)).render(ctx)
    }

    fun salvarEdicaoNoticia(ctx: Context) {
        val noticiaId = ctx.pathParam("noticia")
        val id = noticiaId.toIntOrNull()
            ?: throw IllegalArgumentException("POST editarNoticia: $noticiaId is not an integer.")
        val noticia = dao.getNoticia(id) ?: throw NoSuchElementException("POST editarNoticia: $id is not valid.")

        val title = ctx.formParam("title")
        val html = ctx.formParam("html")

        if (title != null && html != null) {
            noticia.titulo = title
            noticia.html = html
            noticia.dataModificacao = OffsetDateTime.now(brazilZone)
            noticia.ultimaModificacaoPorPessoa = ctx.sessionAttribute<Usuario>("USER")!!.pessoaId
            dao.updateNoticia(noticia)

            ctx.redirect("/admin?editarNoticia=success&id=$id")
            return
        }
        ctx.redirect("/admin?editarNoticia=invalid")
    }

    fun removerPagina(ctx: Context) {
        val linkPagina = ctx.pathParam("pagina")
        dao.getPagina(linkPagina) ?: throw NoSuchElementException("GET removerPagina: $linkPagina is not valid.")

        dao.removePagina(linkPagina)
        ctx.redirect("/admin?removerPagina=success")
    }

    fun removerNoticia(ctx: Context) {
        val noticiaId = ctx.pathParam("noticia")
        val id = noticiaId.toIntOrNull()
            ?: throw IllegalArgumentException("GET removerNoticia: $noticiaId is not an integer.")
        dao.getNoticia(id) ?: throw NoSuchElementException("GET removerNoticia: $id is not valid.")

        dao.removeNoticia(id)
        ctx.redirect("/admin?removerNoticia=success")
    }

    fun novoLink(ctx: Context) {
        val nome = ctx.formParam("nome")
        val href = ctx.formParam("href")
        if (nome != null && href != null) {
            dao.insertLink(nome, href)
            ctx.redirect("/admin?novoLink=success")
            return
        }
        ctx.redirect("/admin?novoLink=invalid")
    }

    fun swapLinks(ctx: Context) {
        val firstId = ctx.queryParam("i")?.toIntOrNull()
        val secondId = ctx.queryParam("j")?.toIntOrNull()
        if (firstId != null && secondId != null) {
            val first = dao.getLink(firstId)
            val second = dao.getLink(secondId)

            if (first != null && second != null) {
                dao.swapLinks(first to second)
                ctx.redirect("/admin?swapLinks=success")
                return
            }
        }
        ctx.redirect("/admin?swapLinks=invalid")
    }

    fun deleteLink(ctx: Context) {
        val id = ctx.queryParam("i")?.toIntOrNull()
        if (id != null) {
            val first = dao.getLink(id)

            if (first != null) {
                dao.removeLink(id)
                ctx.redirect("/admin?deleteLink=success")
                return
            }
        }
        ctx.redirect("/admin?deleteLink=invalid")
    }
}