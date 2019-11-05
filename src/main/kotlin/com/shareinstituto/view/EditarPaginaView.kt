package com.shareinstituto.view

import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*

class EditarPaginaView(dao: DataAccessObject) : AdminModeloView(dao) {
    override val pageTitle = "Summernote Exemplo"
    override val extraCss = listOf("/css/administração.css")
    override val mainPage = "/admin"

    override fun MAIN.renderMain(ctx: Context) {
        div(classes = "summernote")
        form(method = FormMethod.post) {
            input() {
                id = "summernote"
            }
        }
    }

    override fun HEAD.extraLinks() {
        link(href = "https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-lite.css", rel = "stylesheet")
        script(src = "https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-lite.js") {}
    }

    override fun BODY.extraScripts() {
        script(src = "http://code.jquery.com/jquery-3.4.1.min.js") {}
        script {
            unsafe {
                +"""
                $(document).ready(function() {
                    $('#summernote').summernote();
                })
                """.trimIndent()
            }
        }
    }
}
//