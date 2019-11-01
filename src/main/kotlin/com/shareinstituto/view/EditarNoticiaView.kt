package com.shareinstituto.view

import com.shareinstituto.model.dao.MainDao
import io.javalin.http.Context
import kotlinx.html.*

class EditarNoticiaView(dao: MainDao) : ModeloView(dao) {
    override val pageTitle = "Summernote Exemplo"
    override val extraCss = listOf("/css/administração.css")
    override val mainPage = "/admin"

    override fun MAIN.main(ctx: Context) {
        div(classes = "summernote")
        form(method = FormMethod.post) {
            div("row") {
                div(classes = "input-field col s8") {
                    input(type = InputType.text, classes = "validate") {
                        placeholder = "Título"
                        id = "inputTitle"
                    }
                    label {
                        htmlFor = "inputTitle"

                        +"Título da Página"
                    }
                }
                div(classes = "input-field col s4") {
                    input(type = InputType.text, classes = "validate") {
                        placeholder = "Link da Página"
                        id = "inputTitle"
                    }
                    label {
                        htmlFor = "inputTitle"

                        +"Título da Página"
                    }
                }
            }

            input() {
                id = "summernote"
            }

            button(classes = "btn waves-effect waves-light", type = ButtonType.submit, name = "action") {
                +"Submit"
                i("material-icons right") {
                    +"send"
                }
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