package com.shareinstituto.model.page

import com.fasterxml.jackson.databind.ObjectMapper
import com.shareinstituto.model.Noticia

class EditarNoticiaViewModel(
    val mapper: ObjectMapper,
    val noticia: Noticia?,
    val editing: Boolean
) : PagIniAdminModel()