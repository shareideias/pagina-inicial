package com.shareinstituto.model.dao

import com.shareinstituto.model.*
import java.security.MessageDigest
import java.util.*

interface DataAccessObject {
    fun getUsuario(username: String): Usuario?
    fun getPessoa(id: Int): Pessoa?
    fun getNoticia(codigo: Int): Noticia?
    fun getPagina(link: String): Pagina?
    fun getLink(ordinal: Int): Link?

    fun allUsuarios(): List<Usuario>
    fun allPessoas(): List<Pessoa>
    fun allNoticias(): List<Noticia>
    fun allPaginas(): List<Pagina>
    fun allLinks(): List<Link>


    fun insertUsuario(username: String, password: String, pessoaId: Int, admin: Boolean): Usuario
    fun insertPessoa(nome: String): Pessoa
    fun insertNoticia(titulo: String, html: String, criadoPorPessoa: Int): Noticia
    fun insertPagina(linkPagina: String, titulo: String, html: String, criadoPorPessoa: Int): Pagina
    fun insertLink(ordinal: Int, nome: String, href: String): Link

    fun updateUsuario(usuario: Usuario)
    fun updatePessoa(pessoa: Pessoa)
    fun updateNoticia(noticia: Noticia)
    fun updatePagina(pagina: Pagina)
    fun updateLink(link: Link)

    fun removeUsuario(username: String)
    fun removePessoa(id: Int)
    fun removeNoticia(id: Int)
    fun removePagina(link: String)
    fun removeLink(ordinal: Int)

    fun paginateNoticias(pagina: Int): List<Noticia>

    companion object {
        fun hashPassword(password: String): String {
            return Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            )
        }
    }
}