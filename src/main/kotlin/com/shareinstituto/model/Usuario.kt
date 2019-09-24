package com.shareinstituto.model

import java.time.OffsetDateTime

data class Usuario(
    var username: String,
    var nome: String,
    var hash: String,
    val dataCriacao: OffsetDateTime = OffsetDateTime.now()
)

