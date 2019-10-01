package com.shareinstituto.model.dao

import com.shareinstituto.model.Link
import com.shareinstituto.model.Noticia
import com.shareinstituto.model.Pagina
import com.shareinstituto.model.Usuario

class MemoryMainDao : MainDao {
    override fun getLink(codigo: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allUsuarios(): List<Usuario> {
        return usuarios.values.sortedByDescending { it.dataCriacao }
    }

    override fun allLinks(): List<Link> {
        return links.values.sortedBy { it.ordinal }
    }

    override fun removeLink(codigo: Int) {
        links.remove(codigo)
    }

    override fun updateLink(link: Link) {
        links[link.ordinal] = link
    }

    val usuarios = LinkedHashMap<String, Usuario>()
    val paginas = LinkedHashMap<String, Pagina>()
    val noticias = LinkedHashMap<Int, Noticia>()
    val links = LinkedHashMap<Int, Link>()

    override fun getNoticia(codigo: Int): Noticia? = noticias[codigo]

    override fun getUsuario(username: String): Usuario? = usuarios[username]

    override fun getPagina(link: String): Pagina? = paginas[link]

    override fun removeNoticia(codigo: Int) {
        noticias.remove(codigo)
    }

    override fun removeUsuario(username: String) {
        usuarios.remove(username)
    }

    override fun removePagina(link: String) {
        paginas.remove(link)
    }

    override fun updateNoticia(noticia: Noticia) {
        noticias[noticia.codigo] = noticia
    }

    override fun updateUsuario(usuario: Usuario) {
        usuarios[usuario.username] = usuario
    }

    override fun updatePagina(pagina: Pagina) {
        paginas[pagina.linkPagina] = pagina
    }

    override fun paginateNoticias(pagina: Int): List<Noticia> {
        return noticias.values.asSequence()
            .sortedByDescending { it.dataModificacao ?: it.dataCriacao }
            .drop(pagina * 10)
            .take(10)
            .toList()
    }

}