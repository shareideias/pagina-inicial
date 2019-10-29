package com.shareinstituto.model.dao

import com.shareinstituto.model.Link
import com.shareinstituto.model.Noticia
import com.shareinstituto.model.Pagina
import com.shareinstituto.model.Usuario
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.jackson2.Jackson2Plugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

class JdbiDao : MainDao {
    val jdbi = Jdbi.create("jdbc:postgresql:adriantodt")
        .installPlugin(PostgresPlugin())
        .installPlugin(Jackson2Plugin())
        .installPlugin(KotlinPlugin())
        .installPlugin(KotlinSqlObjectPlugin())

    init {
        jdbi.useHandleUnchecked {
            it.execute("""
                CREATE TABLE IF NOT EXISTS usuario(
                    id SERIAL PRIMARY KEY,
                    username TEXT UNIQUE,
                    nome TEXT NOT NULL,
                    hash TEXT NOT NULL,
                    dataCriacao TIMESTAMP NOT NULL
                )
            """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS noticia(
                    id SERIAL PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    html TEXT NOT NULL,
                    criadoPorUsuario INT REFERENCES usuario(id) ON DELETE NO ACTION,
                    dataCriacao TIMESTAMP NOT NULL DEFAULT NOW,
                    dataModificacao TIMESTAMP DEFAULT NULL,
                    ultimaModificacaoPorUsuario INT REFERENCES usuario(id) ON DELETE NO ACTION
                )
            """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS pagina(
                    linkPagina TEXT PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    html TEXT NOT NULL,
                    criadoPorUsuario INT REFERENCES usuario(id) ON DELETE NO ACTION,
                    dataCriacao TIMESTAMP NOT NULL DEFAULT NOW,
                    dataModificacao TIMESTAMP DEFAULT NULL,
                    ultimaModificacaoPorUsuario INT REFERENCES usuario(id) ON DELETE NO ACTION
                )
            """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS link(
                    ordinal INT PRIMARY KEY,
                    nome TEXT NOT NULL,
                    link TEXT NOT NULL
                )
            """.trimIndent())
        }
    }

    override fun getNoticia(codigo: Int): Noticia? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUsuario(username: String): Usuario? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPagina(link: String): Pagina? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLink(codigo: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allUsuarios(): List<Usuario> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allLinks(): List<Link> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeNoticia(codigo: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeUsuario(username: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removePagina(link: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeLink(codigo: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNoticia(noticia: Noticia) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUsuario(usuario: Usuario) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePagina(pagina: Pagina) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateLink(link: Link) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun paginateNoticias(pagina: Int): List<Noticia> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}