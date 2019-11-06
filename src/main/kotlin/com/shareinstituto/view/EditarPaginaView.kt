package com.shareinstituto.view

import com.fasterxml.jackson.databind.ObjectMapper
import com.shareinstituto.model.Pagina
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.base.PagIniAdminView
import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.ButtonType.submit
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.text

class EditarPaginaView(private val mapper: ObjectMapper, dao: DataAccessObject, private val pagina: Pagina?) : PagIniAdminView(dao) {
    override val pageTitle = if (pagina == null) "Nova Página" else "Edição de Página"

    override fun MAIN.renderMain(ctx: Context) {
        div("container") {
            h5("underlined") { +pageTitle }
            form(method = post) {
                div("row") {
                    div("input-field col s8") {
                        label {
                            htmlFor = "inputTitle"
                            +"Título da página"
                        }
                        input(text, classes = "validate", name = "title") {
                            id = "inputTitle"
                            placeholder = "Título"
                            pagina?.let { value = it.titulo }
                        }
                    }

                    div("input-field col s4") {
                        label {
                            htmlFor = "inputLink"
                            +"Link da página"
                        }
                        input(text, classes = "validate", name = "linkPagina") {
                            id = "inputLink"
                            placeholder = "link_da_pagina"
                            pagina?.let { value = it.linkPagina }
                        }
                    }

                    div("col s12") {
                        label { +"Corpo da página" }
                        textArea(classes = "materialize-textarea") {
                            id = "summernote"
                            name = "html"
                        }
                    }

                    div("col s12 input-field") {
                        button(type = submit, classes = "btn waves-effect light-blue lighten-2") {
                            +if (pagina == null) "Criar Página" else "Editar Página"
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

                pagina?.let {
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