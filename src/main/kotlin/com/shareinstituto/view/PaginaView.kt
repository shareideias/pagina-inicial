package com.shareinstituto.view

import com.shareinstituto.model.Pagina
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.base.PagIniView
import com.shareinstituto.view.base.PagIniView.Type.PUBLIC_PAGE
import io.javalin.http.Context
import kotlinx.html.MAIN
import kotlinx.html.article
import kotlinx.html.h3
import kotlinx.html.unsafe

class PaginaView(dao: DataAccessObject, private val pagina: Pagina) : PagIniView(dao, PUBLIC_PAGE) {
    override val pageTitle = pagina.titulo

    override fun MAIN.renderMain(ctx: Context) {
        article {
            h3 { +pagina.titulo }
            unsafe { +pagina.html }
        }
    }
}