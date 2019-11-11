package com.shareinstituto.model.page

import com.fasterxml.jackson.databind.ObjectMapper
import com.shareinstituto.model.Pagina

class EditarPaginaViewModel(
    val mapper: ObjectMapper,
    val pagina: Pagina?,
    val editing: Boolean
) : PagIniAdminModel()