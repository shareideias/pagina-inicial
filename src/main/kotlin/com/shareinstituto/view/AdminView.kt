package com.shareinstituto.view

import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME

class AdminView(dao: DataAccessObject) : AdminModeloView(dao) {
    override val pageTitle = "Administração"
    override val extraCss = listOf("/css/administração.css")
    override val mainPage = "/admin"

    private fun DIV.collumn1() {
        h5 { +"Notícias" }

        ul(classes = "collection") {
            a("/novaNoticia", classes = "collection-item") { +"Nova Notícia" }

            for (noticia in dao.allNoticias()) {
                li(classes = "collection-item avatar") {
                    i(classes = "material-icons circle") { +"receipt" }
                    span(classes = "title") { +noticia.titulo }
                    p {
                        +"Criado por "
                        +(dao.getPessoa(noticia.criadoPorPessoa)?.nome ?: "Usuário Removido")
                        +" em "
                        b { +RFC_1123_DATE_TIME.format(noticia.dataCriacao) }
                    }
                    a(href = "/editNoticia/${noticia.id}", classes = "secondary-content") {
                        i(classes = "material-icons") { +"edit" }
                    }
                }
            }
        }

    }

    private fun DIV.collumn2() {

        h5 { +"Páginas" }

        ul(classes = "collection") {
            a("/novaPagina", classes = "collection-item") { +"Nova Página" }

            for (pagina in dao.allPaginas()) {
                li(classes = "collection-item avatar") {
                    i(classes = "material-icons circle") { +"web" }
                    span(classes = "title") { +pagina.titulo }
                    p {
                        +"Criado por "
                        +(dao.getPessoa(pagina.criadoPorPessoa)?.nome ?: "Usuário Removido")
                        +" em "
                        b { +RFC_1123_DATE_TIME.format(pagina.dataCriacao) }
                    }
                    a(href = "/editPagina/${pagina.linkPagina}", classes = "secondary-content") {
                        i(classes = "material-icons") { +"edit" }
                    }
                }
            }
        }

    }

    private fun DIV.collumn3() {
        h5 { +"Links" }

        ul(classes = "collection") {
            a("/novoLink", classes = "collection-item") { +"Novo Link" }

            for (link in dao.allLinks()) {
                li(classes = "collection-item avatar") {
                    i(classes = "material-icons circle") { +"short_text" }
                    span(classes = "title") { +link.nome }
                    p {
                        +"Aponta para "
                        code("blue lighten-5") { a(href = link.href) { +link.href } }
                    }
                    a(href = "/editLink/${link.ordinal}", classes = "secondary-content") {
                        i(classes = "material-icons") { +"edit" }
                    }
                }
            }
        }
    }


    override fun MAIN.renderMain(ctx: Context) {
        h2("Administração")

        div(classes = "container") {
            div(classes = "row") {
                div("col s12 m4") { collumn1() }
                div("col s12 m4") { collumn2() }
                div("col s12 m4") { collumn3() }
            }
        }

        div(classes = "container") {
            div(classes = "row") {
                div(classes = "col 14 m6 s12") {
                    h3(classes = "news_title") {
                        +"Páginas"
                    }
                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
                            }
                            span(classes = "title") {
                                +"Sobre nós"
                            }
                            p {
                                +"A Share - Centro de Línguas é uma entidade estudantil da UFSCar - Campus Sorocaba que surgiu"
                                +" no ano de 2017, com o intuito de disponibilizar o ensino de línguas para os alunos da"
                                +" universidade. Atualmente, além de oferecermos diversos cursos de línguas ainda atuamos em outras"
                                +" áreas (como dança e fotografia) e visamos sempre atingir novos objetivos."
                            }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"web"
                            }
                            span(classes = "title") {
                                +"Institucional"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"web"
                            }
                            span(classes = "title") {
                                +"Cursos"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"web"
                            }
                            span(classes = "title") {
                                +"Inscrições"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}