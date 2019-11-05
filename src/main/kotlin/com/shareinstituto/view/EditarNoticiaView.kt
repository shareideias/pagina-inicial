package com.shareinstituto.view

import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*

class EditarNoticiaView(dao: DataAccessObject) : AdminModeloView(dao) {
    override val pageTitle = "Summernote Exemplo"
    override val extraCss = listOf("/css/administração.css")
    override val mainPage = "/admin"

    override fun MAIN.renderMain(ctx: Context) {
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