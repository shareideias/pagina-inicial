package com.shareinstituto.model.page

import com.shareinstituto.model.Link
import com.shareinstituto.model.Noticia
import com.shareinstituto.model.Pessoa

class NoticiaViewModel(
    navLinks: List<Link>,
    val noticia: Noticia,
    val criadoPorPessoa: Pessoa?,
    val ultimaModificacaoPorPessoa: Pessoa?
) : PagIniModel(navLinks)