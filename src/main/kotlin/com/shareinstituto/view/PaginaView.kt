package com.shareinstituto.view

import com.shareinstituto.model.Pagina
import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*

class PaginaView(dao: DataAccessObject, private val pagina: Pagina) : ModeloView(dao) {
    override val pageTitle = pagina.titulo
    override val extraCss = listOf("/css/pagina.css")
    override val mainPage = "/"

    override fun MAIN.renderMain(ctx: Context) {
        article {
            h3 { +pagina.titulo }
            unsafe { +pagina.html }
        }
    }

    override fun BODY.extraScripts() {
        footer(classes = "rodape_pag") {
            ul(classes = "redes_sociais") {
                li { a("https://www.facebook.com/shareideias/", classes = "facebook") { +"Facebook" } }
                li { a("https://www.instagram.com/shareideias/", classes = "instagram") { +"Instagram" } }
                li { a("https://www.linkedin.com/in/shareideias/", classes = "linkedin") { +"LinkedIn" } }
            }
            p { +"© Share. Todos os direitos reservados." }
        }
    }
}