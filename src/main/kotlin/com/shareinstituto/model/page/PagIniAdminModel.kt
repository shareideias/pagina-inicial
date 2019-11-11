package com.shareinstituto.model.page

import com.shareinstituto.model.Link

private val adminNavLinks = listOf(Link(0, "Ver Site", "/"), Link(1, "Logout", "/logout"))

open class PagIniAdminModel : PagIniModel(adminNavLinks) {
    companion object : PagIniAdminModel()
}