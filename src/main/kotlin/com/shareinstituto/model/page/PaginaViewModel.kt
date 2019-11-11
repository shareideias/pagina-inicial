package com.shareinstituto.model.page

import com.shareinstituto.model.Link
import com.shareinstituto.model.Pagina

class PaginaViewModel(
    navLinks: List<Link>,
    val pagina: Pagina
) : PagIniModel(navLinks)