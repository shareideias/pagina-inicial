package com.shareinstituto.view.base

import com.shareinstituto.model.Link
import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*

abstract class PagIniView(val dao: DataAccessObject, val type: Type) : HtmlBuilderView() {
    enum class Type(val cssModules: List<String>) {
        INDEX(listOf("public", "index")),
        LOGIN(listOf("login")),
        PUBLIC_PAGE(listOf("public")),
        ADMIN_PAGE(emptyList())
    }

    protected open val pageTitle: String? = null
    protected open val mainPage: String = "/"

    override fun HTML.render(ctx: Context) {
        head {
            meta(charset = "utf-8")
            meta("viewport", "width=device-width, initial-scale=1.0")

            link("https://fonts.googleapis.com/icon?family=Material+Icons", "stylesheet")
            link("/css/materialize.min.css", "stylesheet")
            link("/css/pagini.css", "stylesheet")
            for (module in type.cssModules) {
                link("/css/pagini_$module.css", "stylesheet")
            }
            afterLinks(ctx)

            if (pageTitle != null) {
                title("Associação Share · $pageTitle")
            } else {
                title("Associação Share")
            }

            link("/img/globo.png", "icon")
        }
        body {
            header { renderHeader() }
            main { renderMain(ctx) }
            if ("public" in type.cssModules) {
                footer("public-footer") { renderFooter() }
            }
            beforeScripts(ctx)
            renderScripts()
            afterScripts(ctx)
        }
    }

    protected open fun HEAD.afterLinks(ctx: Context) {
    }

    protected open fun navLinks(): List<Link> {
        return dao.allLinks()
    }

    protected open fun MAIN.renderMain(ctx: Context) {
    }

    protected open fun BODY.beforeScripts(ctx: Context) {
    }

    protected open fun BODY.afterScripts(ctx: Context) {
    }

    private fun HEADER.renderHeader() {
        nav("nav-wrapper transparent") {
            div("container") {
                a(mainPage, classes = "brand-logo") {
                    img("Logo da Share", "/img/globo.png", "share-brand")
                }
                a("#", classes = "sidenav-trigger") {
                    attributes["data-target"] = "mobile-menu"
                    i("material-icons") { +"menu" }
                }

                val navLinks = navLinks()

                ul("right hide-on-med-and-down") {
                    navLinks.forEach { li { a(it.href, classes = "link_menu") { +it.nome } } }
                }
                ul("sidenav lighten-2") {
                    id = "mobile-menu"
                    navLinks.forEach { li { a(it.href) { +it.nome } } }
                }
            }

        }
        if (type == Type.INDEX) {
            img("'Share' em Manuscrito", "/img/share-handwritten.png", "handwritten-logo")
        }
    }

    private fun FOOTER.renderFooter() {
        ul("social-networks") {
            li { a("https://www.facebook.com/shareideias/", classes = "img-fb") { +"Facebook" } }
            li { a("https://www.instagram.com/shareideias/", classes = "img-ig") { +"Instagram" } }
            li { a("https://www.linkedin.com/in/shareideias/", classes = "img-ln") { +"LinkedIn" } }
        }
        p("center-align bold") { +"© Share. Todos os direitos reservados." }
    }

    private fun BODY.renderScripts() {
        script(src = "/js/materialize.min.js") {}
        script(src = "/js/m_sidenav.js") {}
    }
}