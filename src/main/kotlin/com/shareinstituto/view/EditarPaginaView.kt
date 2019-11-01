package com.shareinstituto.view

import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*

class EditarPaginaView(dao: DataAccessObject) : ModeloView(dao) {
    override val pageTitle = "Summernote Exemplo"
    override val extraCss = listOf("/css/administração.css")
    override val mainPage = "/admin"

    override fun MAIN.main(ctx: Context) {
        div(classes = "summernote")
        form(method = FormMethod.post) {
            input() {
                id = "summernote"
            }
        }

        script {
            unsafe {
                +"""
                    $(document).ready(function() {
                        $('#summernote').summernote()
                    })
                """.trimIndent()
            }

        }
    }
}
//