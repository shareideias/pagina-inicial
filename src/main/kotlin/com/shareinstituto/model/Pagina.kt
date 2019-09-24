package com.shareinstituto.model

import java.time.OffsetDateTime

data class Pagina(
    val linkPagina: String,
    var titulo: String,
    var html: String,
    val criadoPorUsuario: Int,
    val dataCriacao: OffsetDateTime = OffsetDateTime.now(),
    var dataModificacao: OffsetDateTime? = null,
    var ultimaModificacaoPorUsuario: Int? = null
)