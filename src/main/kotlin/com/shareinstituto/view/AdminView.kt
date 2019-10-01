package com.shareinstituto.view

import com.shareinstituto.model.dao.MainDao
import io.javalin.http.Context
import kotlinx.html.*


class AdminView(val dao: MainDao) : HtmlBuilderView() {
    override fun HTML.render(ctx: Context) {
        head {
            link("https://fonts.googleapis.com/icon?family=Material+Icons", rel = "stylesheet")
            link(type = "text/css", rel = "stylesheet", href = "/css/materialize.min.css") {
                media = "screen,projection"
            }

            meta("viewport", "width=device-width, initial-scale=1.0")
            meta(charset = "utf-8")

            title("Share - Administração")

            link(rel = "icon", href = "/img/globo.png")
            link(rel = "stylesheet", href = "/css/administração.css")

        }
        body {
            header { header() }
            main { main() }
            footer(classes = "rodape_pag") { footer() }
            scripts()
        }
    }

    private fun HEADER.header() {
        nav(classes = "nav-wrapper transparent") {
            div(classes = "container") {
                a(href = "index.html", classes = "brand-logo") {
                    img(classes = "imagem_logo", alt = "Logo da Share", src = "/img/globo.png")
                }
                a(href = "", classes = "sidenav-trigger") {
                    attributes["data-target"] = "mobile-menu"
                    i(classes = "material-icons") { +"menu" }

                }
                val links = dao.allLinks()

                ul(classes = "right hide-on-med-and-down") {
                    for (link in links) {
                        li { a(classes = "link_menu", href = link.href) { +link.nome } }
                    }
                }
                ul(classes = "sidenav lighten-2") {
                    id = "mobile-menu"

                    for (link in links) {
                        li { a(href = link.href) { +link.nome } }
                    }
                }
            }

        }

    }

    private fun MAIN.main() {
        h2("Administração")

        div(classes = "container") {
            div(classes = "row") {
                div(classes = "col 14 m6 s12") {
                    h3(classes = "news_title") {
                        +"Recentes"
                    }
                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"short_text"
                            }
                            span(classes = "title") {
                                +"Recente 1"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"short_text"
                            }
                            span(classes = "title") {
                                +"Recente 2"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"short_text"
                            }
                            span(classes = "title") {
                                +"Recente 3"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    h3(classes = "news_title") {
                        +"Notícias"
                    }
                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 1"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 2"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 3"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }


                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 4"
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
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
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
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
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
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
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

    private fun FOOTER.footer() {
        ul(classes = "redes_sociais") {
            li { a("https://www.facebook.com/shareideias/", classes = "facebook") { +"Facebook" } }
            li { a("https://www.instagram.com/shareideias/", classes = "instagram") { +"Instagram" } }
            li { a("https://www.linkedin.com/in/shareideias/", classes = "linkedin") { +"LinkedIn" } }
        }
        p { +"© Share. Todos os direitos reservados." }
    }

    private fun BODY.scripts() {
        script(src = "/js/materialize.min.js") {}
        script {
            unsafe {
                +"""
                    document.addEventListener('DOMContentLoaded', function() {
                        var elems = document.querySelectorAll('.sidenav');
                        var instances = M.Sidenav.init(elems, options);
                    });
                """.trimIndent()
            }
        }
    }
}