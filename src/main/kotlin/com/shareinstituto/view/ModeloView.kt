package com.shareinstituto.view

import com.shareinstituto.model.dao.MainDao
import io.javalin.http.Context
import kotlinx.html.*

abstract class ModeloView(val dao: MainDao) : HtmlBuilderView() {
    abstract val pageTitle: String
    abstract val extraCss: List<String>
    abstract val mainPage: String

    override fun HTML.render(ctx: Context) {
        head {
            meta(charset = "utf-8")
            meta("viewport", "width=device-width, initial-scale=1.0")

            link("https://fonts.googleapis.com/icon?family=Material+Icons", "stylesheet")
            link("/css/materialize.min.css", "stylesheet")
            link("/css/modelo.css", "stylesheet")

            title("Share - $pageTitle")
            link("/img/globo.png", "icon")

            extraCss.forEach { link(it, "stylesheet") }
        }
        body {
            header { header() }
            main { main(ctx) }
            footer("rodape_pag") { footer() }
            scripts()
        }
    }

    private fun HEADER.header() {
        nav("nav-wrapper transparent") {
            div("container") {
                a(href = mainPage, classes = "brand-logo") {
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

    abstract fun MAIN.main(ctx: Context)

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
