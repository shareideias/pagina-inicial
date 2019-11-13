package br.com.associacaoshare.model.page

import br.com.associacaoshare.model.Link
import br.com.associacaoshare.model.Noticia
import br.com.associacaoshare.model.Pagina
import br.com.associacaoshare.model.Pessoa

class AdminViewModel(
    val noticias: List<Noticia>,
    val paginas: List<Pagina>,
    val pessoas: Map<Int, Pessoa>,
    val links: List<Link>
) : PagIniAdminModel()