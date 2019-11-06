package com.shareinstituto.view.base

import com.shareinstituto.model.Link
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.base.PagIniView.Type.ADMIN_PAGE

abstract class PagIniAdminView(dao: DataAccessObject) : PagIniView(dao, ADMIN_PAGE) {
    override val mainPage = "/admin"

    override fun navLinks(): List<Link> {
        return adminNavLinks
    }

    companion object {
        private val adminNavLinks = listOf(Link(0, "Ver Site", "/"), Link(1, "Logout", "/logout"))
    }
}