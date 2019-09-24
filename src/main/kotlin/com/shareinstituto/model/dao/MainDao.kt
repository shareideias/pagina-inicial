package com.shareinstituto.model.dao

import com.shareinstituto.model.Link
import com.shareinstituto.model.Noticia
import com.shareinstituto.model.Pagina
import com.shareinstituto.model.Usuario

interface MainDao {
    fun getNoticia(codigo: Int): Noticia?
    fun getUsuario(username: String): Usuario?
    fun getPagina(link: String): Pagina?
    fun getLink(codigo: Int)

    fun allUsuarios(): List<Usuario>
    fun allLinks(): List<Link>

    fun removeNoticia(codigo: Int)
    fun removeUsuario(username: String)
    fun removePagina(link: String)
    fun removeLink(codigo: Int)

    fun updateNoticia(noticia: Noticia)
    fun updateUsuario(usuario: Usuario)
    fun updatePagina(pagina: Pagina)
    fun updateLink(link: Link)

    fun paginateNoticias(pagina: Int): List<Noticia>
}