package com.shareinstituto.view

import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*

abstract class ModeloView(val dao: DataAccessObject) : HtmlBuilderView() {
    abstract val pageTitle: String
    open val extraCss: List<String> = emptyList()
    abstract val mainPage: String

    override fun HTML.render(ctx: Context) {
        head {
            meta(charset = "utf-8")
            meta("viewport", "width=device-width, initial-scale=1.0")

            link("https://fonts.googleapis.com/icon?family=Material+Icons", "stylesheet")
            link("/css/materialize.min.css", "stylesheet")
            link("/css/modelo.css", "stylesheet")
            extraLinks()
            title("Associação Share · $pageTitle")
            link("/img/globo.png", "icon")

            extraCss.forEach { link(it, "stylesheet") }
        }
        body {
            header { renderHeader() }
            main { renderMain(ctx) }
            scripts()
        }
    }

    open fun links() = dao.allLinks()

    private fun HEADER.renderHeader() {
        nav("nav-wrapper transparent") {
            div("container") {
                a(href = mainPage, classes = "brand-logo") {
                    img(classes = "imagem_logo", alt = "Logo da Share", src = "/img/globo.png")
                }
                a(href = "#", classes = "sidenav-trigger") {
                    attributes["data-target"] = "mobile-menu"
                    i(classes = "material-icons") { +"menu" }
                }
                val links = links()

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

    abstract fun MAIN.renderMain(ctx: Context)

    open fun HEAD.extraLinks() = Unit
    open fun BODY.extraScripts() = Unit

    private fun BODY.scripts() {
        extraScripts()
        script(src = "/js/materialize.min.js") {}
        script {
            unsafe {
                +"""
                    document.addEventListener('DOMContentLoaded', function() {
                        var elems = document.querySelectorAll('.sidenav');
                        var instances = M.Sidenav.init(elems, {});
                    });
                """.trimIndent()
            }
        }
    }
}
