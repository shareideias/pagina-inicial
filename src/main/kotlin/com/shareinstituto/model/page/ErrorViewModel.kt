package com.shareinstituto.model.page

import com.shareinstituto.model.Link

private val adminLinks = listOf(Link(0, "Administração", "/admin"))
private val publicLinks = listOf(Link(0, "Início", "/"))

class ErrorViewModel(
    val isAdmin: Boolean,
    val errorCode: Int,
    val errorTitle: String,
    val errorDescription: String
) : PagIniModel(if (isAdmin) adminLinks else publicLinks)