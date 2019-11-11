package com.shareinstituto.view

import com.shareinstituto.model.page.AdminViewModel
import com.shareinstituto.utils.forEachNeighbouring
import com.shareinstituto.view.base.PagIniAdminView
import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME

class AdminView(override val model: AdminViewModel) : PagIniAdminView() {
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

            for (noticia in model.noticias) {
                li("collection-item avatar") {
                    i("material-icons circle") { +"receipt" }
                    val link = "/n/${noticia.id}"
                    span("title") {
                        a(link) {
                            +noticia.titulo
                            small {
                                +" (Local: "
                                code("blue lighten-5") { +link }
                                +")"
                            }
                        }
                    }
                    p {
                        +"Criado por "
                        b { +(model.pessoas[noticia.criadoPorPessoa]?.nome ?: "Usuário removido") }
                    }
                    p {
                        +"Em "
                        b { +RFC_1123_DATE_TIME.format(noticia.dataCriacao) }
                    }
                    if (noticia.ultimaModificacaoPorPessoa != null && noticia.dataModificacao != null) {
                        p {
                            +"Ultima modificação por "
                            b { +(model.pessoas[noticia.ultimaModificacaoPorPessoa!!]?.nome ?: "Usuário removido") }
                        }
                        p {
                            +"Em "
                            b { +RFC_1123_DATE_TIME.format(noticia.dataModificacao!!) }
                        }
                    }
                    div("secondary-content") {
                        a("/admin/editarNoticia/${noticia.id}") {
                            i("material-icons") { +"edit" }
                        }
                        a("/admin/removerNoticia/${noticia.id}") {
                            i("material-icons") { +"delete" }
                        }
                    }
                }
            }
        }

    }

    private fun DIV.collumn2() {
        h5 { +"Páginas" }

        ul("collection") {
            a("/admin/novaPagina", classes = "collection-item") { +"Nova Página" }

            for (pagina in model.paginas) {
                li("collection-item avatar") {
                    i("material-icons circle") { +"web" }
                    val link = "/p/${pagina.linkPagina}"
                    span("title") {
                        a(link) {
                            +pagina.titulo
                            small {
                                +" (Local: "
                                code("blue lighten-5") { +link }
                                +")"
                            }
                        }
                    }
                    p {
                        +"Criado por "
                        b { +(model.pessoas[pagina.criadoPorPessoa]?.nome ?: "Usuário removido") }
                    }
                    p {
                        +"Em "
                        b { +RFC_1123_DATE_TIME.format(pagina.dataCriacao) }
                    }
                    if (pagina.ultimaModificacaoPorPessoa != null && pagina.dataModificacao != null) {
                        p {
                            +"Ultima modificação por "
                            b { +(model.pessoas[pagina.ultimaModificacaoPorPessoa!!]?.nome ?: "Usuário removido") }
                        }
                        p {
                            +"Em "
                            b { +RFC_1123_DATE_TIME.format(pagina.dataModificacao!!) }
                        }
                    }
                    div("secondary-content") {
                        a("/admin/editarPagina/${pagina.linkPagina}") {
                            i("material-icons") { +"edit" }
                        }
                        a("/admin/removerPagina/${pagina.linkPagina}") {
                            i("material-icons") { +"delete" }
                        }
                    }
                }
            }
        }

    }


    private fun DIV.collumn3() {
        h5 { +"Links" }

        ul("collection") {
            model.links.forEachNeighbouring { (link, before, after) ->
                li("collection-item avatar") {
                    i("material-icons circle") { +"short_text" }
                    span("title") { +link.nome }
                    p {
                        +"Aponta para "
                        code("blue lighten-5") { a(link.href) { +link.href } }
                    }
                    div("secondary-content") {
                        if (after != null) {
                            a("/admin/swapLinks?i=${link.id}&j=${after.id}") { i("material-icons") { +"keyboard_arrow_down" } }
                        }
                        if (before != null) {
                            a("/admin/swapLinks?i=${before.id}&j=${link.id}") { i("material-icons") { +"keyboard_arrow_up" } }
                        }
                        a("/admin/deleteLink?i=${link.id}") { i("material-icons") { +"delete" } }
                    }
                }
            }
        }

        br

        div("card") {
            form("/admin/novoLink", method = post, classes = "card-content") {
                span("card-title") { +"Novo Link" }
                div("row mb-0") {
                    div("input-field col s12 mb-0") {
                        label {
                            htmlFor = "inputNome"
                            +"Nome do Link"
                        }
                        input(InputType.text, classes = "validate", name = "nome") {
                            id = "inputNome"
                            placeholder = "Nome"
                        }
                    }

                    div("input-field col s12 mb-0") {
                        label {
                            htmlFor = "inputHref"
                            +"Endereço do Link"
                        }
                        input(InputType.text, classes = "validate", name = "href") {
                            id = "inputHref"
                            placeholder = "Endereço"
                        }
                    }

                    div("input-field col s12 mb-0") {
                        button(type = ButtonType.submit, classes = "btn waves-effect light-blue lighten-2") {
                            +"Criar Link"
                            i("material-icons right") { +"send" }
                        }
                    }
                }
            }
        }

    }
}