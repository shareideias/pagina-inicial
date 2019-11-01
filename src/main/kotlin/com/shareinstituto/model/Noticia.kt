package com.shareinstituto.model

import java.time.OffsetDateTime

data class Noticia(
        val codigo: Int,
        var titulo: String, //[!]
        var html: String, //[!] summernote
        val criadoPorUsuario: Int,
        val dataCriacao: OffsetDateTime = OffsetDateTime.now(),
        var dataModificacao: OffsetDateTime? = null,
        var ultimaModificacaoPorUsuario: Int? = null
)