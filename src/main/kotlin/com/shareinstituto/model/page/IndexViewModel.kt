package com.shareinstituto.model.page

import com.shareinstituto.model.Link
import com.shareinstituto.model.Noticia
import com.shareinstituto.model.Pessoa

class IndexViewModel(
    navLinks: List<Link>,
    val pageNumber: Int,
    val cards: List<Noticia>,
    val noticias: List<Noticia>,
    val pessoas: Map<Int, Pessoa>
) : PagIniModel(navLinks)