package com.shareinstituto.model.dao

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.shareinstituto.model.*
import com.shareinstituto.model.dao.DataAccessObject.Companion.hashPassword
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.*
import org.jdbi.v3.jackson2.Jackson2Config
import org.jdbi.v3.jackson2.Jackson2Plugin
import org.jdbi.v3.postgres.PostgresPlugin

class JdbiDataAccessObject(url: String) : DataAccessObject {
    val jdbi = Jdbi.create(url)
        .installPlugin(PostgresPlugin())
        .installPlugin(Jackson2Plugin())
        .installPlugin(KotlinPlugin())
        .apply { getConfig(Jackson2Config::class.java).mapper.registerKotlinModule() }

    init {
        jdbi.useHandleUnchecked {
            it.execute("""
                CREATE TABLE IF NOT EXISTS pagini_pessoa(
                    id SERIAL PRIMARY KEY,
                    nome TEXT NOT NULL,
                    dataCriacao TIMESTAMP NOT NULL DEFAULT NOW()
                )
                """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS pagini_usuario(
                    username TEXT PRIMARY KEY,
                    hash TEXT NOT NULL,
                    pessoaId INT REFERENCES pagini_pessoa(id) ON DELETE CASCADE,
                    admin BOOLEAN DEFAULT FALSE
                )
                """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS pagini_noticia(
                    id SERIAL PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    html TEXT NOT NULL,
                    criadoPorPessoa INT REFERENCES pagini_pessoa(id) ON DELETE CASCADE,
                    dataCriacao TIMESTAMP NOT NULL DEFAULT NOW(),
                    dataModificacao TIMESTAMP,
                    ultimaModificacaoPorPessoa INT REFERENCES pagini_pessoa(id) ON DELETE CASCADE
                )
                """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS pagini_pagina(
                    linkPagina TEXT PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    html TEXT NOT NULL,
                    criadoPorPessoa INT REFERENCES pagini_pessoa(id) ON DELETE CASCADE,
                    dataCriacao TIMESTAMP NOT NULL DEFAULT NOW(),
                    dataModificacao TIMESTAMP,
                    ultimaModificacaoPorPessoa INT REFERENCES pagini_pessoa(id) ON DELETE CASCADE
                )
                """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS pagini_link(
                    ordinal INT PRIMARY KEY,
                    nome TEXT NOT NULL,
                    href TEXT NOT NULL
                )
                """.trimIndent())

            if (it.createQuery("SELECT COUNT(id) FROM pagini_pessoa").mapTo<Int>().one() < 1) {
                val adminId = insertPessoa("Administrador PágIni").id
                insertUsuario("admin", "admin", adminId, true)
                insertNoticia("Bem-vindo a Share!", """
                    <p align="justify">Seja bem-vindo ao site da Associação Share!<br></p>
                    <p class="par_news" align="justify">A Share é uma Entidade Estudantil fundada em 2016 por alunos de
                    Ciências Econômicas na UFSCar - Campus Sorocaba, com o intuito de conectar a vontade de ensinar com
                    a vontade de aprender. Para isso oferecemos semestralmente cursos de idioma, culturais e
                    administrativos, além de eventos, tudo isso de forma acessível e com certificado. Contamos com
                    professores voluntários e 7 áreas administrativas voluntárias dos quais ajudam a fazer o
                    projeto acontecer e crescer.</p><p class="par_news" align="justify">
                    <i>Essa notícia foi automaticamente criada pelo sistema.<br></i></p>""".trimIndent(), adminId)
            }

            if (it.createQuery("SELECT COUNT(ordinal) FROM pagini_link").mapTo<Int>().one() < 1) {
                insertLink(0, "Home", "/")
                insertLink(1, "Login", "/login")
            }
        }
    }

    override fun getPessoa(id: Int): Pessoa? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_pessoa WHERE id = :id")
                .bind("id", id)
                .mapTo<Pessoa>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getUsuario(username: String): Usuario? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_usuario WHERE username = :username")
                .bind("username", username)
                .mapTo<Usuario>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getNoticia(codigo: Int): Noticia? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_noticia WHERE id = :id")
                .bind("id", codigo)
                .mapTo<Noticia>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getPagina(link: String): Pagina? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_pagina WHERE linkPagina = :link")
                .bind("link", link)
                .mapTo<Pagina>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getLink(ordinal: Int): Link? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_link WHERE ordinal = :ordinal")
                .bind("ordinal", ordinal)
                .mapTo<Link>()
                .findOne()
                .orElse(null)
        }
    }

    override fun allUsuarios(): List<Usuario> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_usuario")
                .mapTo<Usuario>()
                .list()
        }.sortedBy { it.username }
    }

    override fun allPessoas(): List<Pessoa> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_pessoa")
                .mapTo<Pessoa>()
                .list()
        }.sortedBy { it.nome }
    }

    override fun allNoticias(): List<Noticia> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_noticia")
                .mapTo<Noticia>()
                .list()
        }.sortedByDescending { it.dataCriacao }
    }

    override fun allPaginas(): List<Pagina> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_pagina")
                .mapTo<Pagina>()
                .list()
        }.sortedByDescending { it.dataCriacao }
    }

    override fun allLinks(): List<Link> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_link")
                .mapTo<Link>()
                .list()
        }.sortedBy { it.ordinal }
    }

    override fun insertUsuario(username: String, password: String, pessoaId: Int, admin: Boolean): Usuario {
        val hash = hashPassword(password)

        jdbi.useHandleUnchecked {
            it.createUpdate("INSERT INTO pagini_usuario (username, hash, pessoaId, admin) VALUES (:u, :h, :p, :admin)")
                .bind("u", username)
                .bind("h", hash)
                .bind("p", pessoaId)
                .bind("admin", admin)
                .execute()
        }

        return Usuario(username, hash, pessoaId, admin)
    }

    override fun insertPessoa(nome: String): Pessoa {
        val id = jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO pagini_pessoa (nome) VALUES (:nome)")
                .bind("nome", nome)
                .executeAndReturnGeneratedKeys()
                .mapTo<Int>()
                .one()
        }

        return Pessoa(id, nome)
    }

    override fun insertNoticia(titulo: String, html: String, criadoPorPessoa: Int): Noticia {
        val id = jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO pagini_noticia (titulo, html, criadoPorPessoa) VALUES (:t, :html, :u)")
                .bind("t", titulo)
                .bind("html", html)
                .bind("u", criadoPorPessoa)
                .executeAndReturnGeneratedKeys()
                .mapTo<Int>()
                .one()
        }

        return Noticia(id, titulo, html, criadoPorPessoa)
    }

    override fun insertPagina(linkPagina: String, titulo: String, html: String, criadoPorPessoa: Int): Pagina {
        jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO pagini_pagina (linkPagina, titulo, html, criadoPorPessoa) VALUES (:l, :t, :html, :u)")
                .bind("l", linkPagina)
                .bind("t", titulo)
                .bind("html", html)
                .bind("u", criadoPorPessoa)
                .execute()
        }

        return Pagina(linkPagina, titulo, html, criadoPorPessoa)
    }

    override fun insertLink(ordinal: Int, nome: String, href: String): Link {
        jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO pagini_link (ordinal, nome, href) VALUES (:ordinal, :nome, :href)")
                .bind("ordinal", ordinal)
                .bind("nome", nome)
                .bind("href", href)
                .execute()
        }

        return Link(ordinal, nome, href)
    }

    override fun updateUsuario(usuario: Usuario) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE pagini_usuario SET 
                    hash = :hash,
                    pessoaId = :pessoaId,
                    admin = :admin
                WHERE username = :username
            """.trimIndent())
                .bindKotlin(usuario)
                .execute()
        }
    }

    override fun updatePessoa(pessoa: Pessoa) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE pagini_pessoa SET 
                    nome = :nome,
                    dataCriacao = :dataCriacao
                WHERE id = :id
                """.trimIndent())
                .bindKotlin(pessoa)
                .execute()
        }
    }

    override fun updateNoticia(noticia: Noticia) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE pagini_noticia SET 
                    titulo = :titulo,
                    html = :html,
                    criadoPorPessoa = :criadoPorPessoa,
                    dataCriacao = :dataCriacao,
                    dataModificacao = :dataModificacao,
                    ultimaModificacaoPorPessoa = :ultimaModificacaoPorPessoa
                WHERE id = :id
                """.trimIndent())
                .bindKotlin(noticia)
                .execute()
        }
    }

    override fun updatePagina(pagina: Pagina) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE pagini_pagina SET 
                    titulo = :titulo,
                    html = :html,
                    criadoPorPessoa = :criadoPorPessoa,
                    dataCriacao = :dataCriacao,
                    dataModificacao = :dataModificacao,
                    ultimaModificacaoPorPessoa = :ultimaModificacaoPorPessoa
                WHERE linkPagina = :linkPagina
                """.trimIndent())
                .bindKotlin(pagina)
                .execute()
        }
    }

    override fun updateLink(link: Link) {
        jdbi.useHandleUnchecked {
            it.createUpdate("UPDATE pagini_link SET nome = :nome, href = :href WHERE ordinal = :ordinal")
                .bindKotlin(link)
                .execute()
        }
    }

    override fun removeUsuario(username: String) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM pagini_usuario WHERE username = ?", username)
        }
    }

    override fun removePessoa(id: Int) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM pagini_pessoa WHERE id = ?", id)
        }
    }

    override fun removeNoticia(id: Int) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM pagini_noticia WHERE id = ?", id)
        }
    }

    override fun removePagina(link: String) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM pagini_pagina WHERE linkPagina = ?", link)
        }
    }

    override fun removeLink(ordinal: Int) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM pagini_link WHERE ordinal = ?", ordinal)
        }
    }

    override fun paginateNoticias(pagina: Int): List<Noticia> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_noticia ORDER BY id DESC OFFSET :offset LIMIT 7")
                .bind("offset", pagina * 7)
                .mapTo<Noticia>()
                .list()
        }
    }
}