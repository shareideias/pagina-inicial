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

    override fun BODY.scripts() {
        script(src = "http://code.jquery.com/jquery-3.4.1.min.js") {}
        script(src = "/js/materialize.min.js") {}
        script {
            unsafe {
                +"""
                    $(document).ready(function() {
                        $('#summernote').summernote()
                    })
                    
                    document.addEventListener('DOMContentLoaded', function() {
                        var elems = document.querySelectorAll('.sidenav');
                        var instances = M.Sidenav.init(elems, {});
                    });
                """.trimIndent()
            }
        }
    }
}
//