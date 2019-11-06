package com.shareinstituto.view

import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.base.PagIniAdminView
import io.javalin.http.Context
import kotlinx.html.*
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME

class AdminView(dao: DataAccessObject) : PagIniAdminView(dao) {
    override val pageTitle = "Administração"

    override fun MAIN.renderMain(ctx: Context) {
        div("container") {
            h4("underlined") { +"Administração" }
            div("row") {
                div("col s12 m4") { collumn1() }
                div("col s12 m4") { collumn2() }
                div("col s12 m4") { collumn3() }
            }
        }
    }

    private fun DIV.collumn1() {
        h5 { +"Notícias" }

        ul("collection") {
            a("/admin/novaNoticia", classes = "collection-item") { +"Nova Notícia" }

            for (noticia in dao.allNoticias()) {
                li("collection-item avatar") {
                    i("material-icons circle") { +"receipt" }
                    span("title bold") { +noticia.titulo }
                    p {
                        +"Criado por "
                        +(dao.getPessoa(noticia.criadoPorPessoa)?.nome ?: "Usuário Removido")
                        +" em "
                        b { +RFC_1123_DATE_TIME.format(noticia.dataCriacao) }
                    }
                    a("/admin/editNoticia/${noticia.id}", classes = "secondary-content") {
                        i("material-icons") { +"edit" }
                    }
                }
            }
        }

    }

    private fun DIV.collumn2() {
        h5 { +"Páginas" }

        ul("collection") {
            a("/admin/novaPagina", classes = "collection-item") { +"Nova Página" }

            for (pagina in dao.allPaginas()) {
                li("collection-item avatar") {
                    i("material-icons circle") { +"web" }
                    span("title") { +pagina.titulo }
                    p {
                        +"Criado por "
                        +(dao.getPessoa(pagina.criadoPorPessoa)?.nome ?: "Usuário removido")
                        +" em "
                        b { +RFC_1123_DATE_TIME.format(pagina.dataCriacao) }
                    }
                    a("/admin/editPagina/${pagina.linkPagina}", classes = "secondary-content") {
                        i("material-icons") { +"edit" }
                    }
                }
            }
        }

    }


    private fun DIV.collumn3() {
        h5 { +"Links" }

        ul("collection") {
            a("/admin/novoLink", classes = "collection-item") { +"Novo Link" }

            val allLinks = dao.allLinks()
            allLinks.withIndex().forEach { (i, link) ->
                li("collection-item avatar") {
                    i("material-icons circle") { +"short_text" }
                    span("title") { +link.nome }
                    p {
                        +"Aponta para "
                        code("blue lighten-5") { a(link.href) { +link.href } }
                    }
                    div("secondary-content") {
                        if (i != allLinks.lastIndex) {
                            a("/admin/editLink/${link.ordinal}") { i("material-icons") { +"keyboard_arrow_down" } }
                        }
                        if (i != 0) {
                            a("/admin/editLink/${link.ordinal}") { i("material-icons") { +"keyboard_arrow_up" } }
                        }
                        a("/admin/editLink/${link.ordinal}") { i("material-icons") { +"edit" } }
                        a("/admin/editLink/${link.ordinal}") { i("material-icons") { +"delete" } }
                    }
                }
            }
        }
    }
}