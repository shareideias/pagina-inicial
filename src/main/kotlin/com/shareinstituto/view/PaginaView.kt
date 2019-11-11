package com.shareinstituto.view

import com.shareinstituto.model.page.PaginaViewModel
import com.shareinstituto.view.base.PagIniView
import com.shareinstituto.view.base.PagIniView.Type.PUBLIC_PAGE
import io.javalin.http.Context
import kotlinx.html.*

class PaginaView(override val model: PaginaViewModel) : PagIniView(PUBLIC_PAGE) {
    override val pageTitle = model.pagina.titulo

    override fun MAIN.renderMain(ctx: Context) {
        div("container") {
            article {
                h3 { +model.pagina.titulo }
                unsafe { +model.pagina.html }
            }
        }
    }
}