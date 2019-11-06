package com.shareinstituto.view

import com.fasterxml.jackson.databind.ObjectMapper
import com.shareinstituto.model.Noticia
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.base.PagIniAdminView
import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.ButtonType.submit
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.text

class EditarNoticiaView(private val mapper: ObjectMapper, dao: DataAccessObject, private val noticia: Noticia?) : PagIniAdminView(dao) {
    override val pageTitle = if (noticia == null) "Nova Notícia" else "Edição de Notícia"

    override fun MAIN.renderMain(ctx: Context) {
        div("container") {
            h5("underlined") { +"Nova Notícia" }
            form(method = post) {
                div("row") {
                    div("input-field col s12") {
                        label {
                            htmlFor = "inputTitle"
                            +"Título da notícia"
                        }
                        input(text, classes = "validate", name = "title") {
                            id = "inputTitle"
                            placeholder = "Título"
                            noticia?.let { value = it.titulo }
                        }
                    }

                    div("col s12") {
                        label { +"Corpo da página" }
                        textArea(classes ="materialize-textarea") {
                            id = "summernote"
                            name = "html"
                        }
                    }

                    div("col s12 input-field") {
                        button(type = submit, classes = "btn waves-effect light-blue lighten-2") {
                            +if (noticia == null) "Criar Notícia" else "Editar Notícia"
                            i("material-icons right") { +"send" }
                        }
                    }
                }
            }
        }
    }

    override fun HEAD.afterLinks(ctx: Context) {
        link("https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-lite.css", "stylesheet")
    }

    override fun BODY.beforeScripts(ctx: Context) {
        script(src = "http://code.jquery.com/jquery-3.4.1.min.js") {}
    }

    override fun BODY.afterScripts(ctx: Context) {
        script(src = "https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-lite.js") {}
        script(src = "https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/lang/summernote-pt-BR.min.js") {}
        script {
            unsafe {
                +"""
                $(document).ready(function() {
                    $('#summernote').summernote({ height: 400, lang: 'pt-BR' });
                """.trimIndent()

                noticia?.let {
                    +"""
                    $('#summernote').summernote('pasteHTML', ${mapper.writeValueAsString(it.html)});
                    """.trimIndent()
                }
                +"""
                })
                """.trimIndent()
            }
        }
    }
}
//