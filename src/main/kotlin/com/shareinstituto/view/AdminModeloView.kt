package com.shareinstituto.view

import com.shareinstituto.model.Link
import com.shareinstituto.model.dao.DataAccessObject

abstract class AdminModeloView(dao: DataAccessObject) : ModeloView(dao) {
    override fun links() = listOf(Link(0, "Logout", "/logout"))
}