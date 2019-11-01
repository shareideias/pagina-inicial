package com.shareinstituto.model

import java.time.OffsetDateTime

data class Pessoa(
    val id: Int,
    var nome: String,
    val dataCriacao: OffsetDateTime = OffsetDateTime.now()
)

