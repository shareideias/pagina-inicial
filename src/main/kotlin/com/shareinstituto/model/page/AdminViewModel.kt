package com.shareinstituto.model.page

import com.shareinstituto.model.Link
import com.shareinstituto.model.Noticia
import com.shareinstituto.model.Pagina
import com.shareinstituto.model.Pessoa

class AdminViewModel(
    val noticias: List<Noticia>,
    val paginas: List<Pagina>,
    val pessoas: Map<Int, Pessoa>,
    val links: List<Link>
) : PagIniAdminModel()