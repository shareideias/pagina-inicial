package com.shareinstituto.view

import com.shareinstituto.model.dao.MainDao
import io.javalin.http.Context
import kotlinx.html.*

class ModeloView(val dao: MainDao) : HtmlBuilderView() {
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
