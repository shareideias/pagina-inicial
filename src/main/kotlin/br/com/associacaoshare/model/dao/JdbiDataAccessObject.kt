package br.com.associacaoshare.model.dao

import br.com.associacaoshare.model.*
import br.com.associacaoshare.model.dao.DataAccessObject.Companion.hashPassword
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.*
import org.jdbi.v3.jackson2.Jackson2Config
import org.jdbi.v3.jackson2.Jackson2Plugin
import org.jdbi.v3.postgres.PostgresPlugin
import java.time.OffsetDateTime

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
                    pessoaId INT UNIQUE REFERENCES pagini_pessoa(id) ON DELETE CASCADE,
                    admin BOOLEAN DEFAULT FALSE
                )
                """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS pagini_noticia(
                    id SERIAL PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    html TEXT NOT NULL,
                    criadoPorPessoa INT REFERENCES pagini_pessoa(id) ON DELETE CASCADE,
                    dataCriacao TIMESTAMP NOT NULL,
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
                    dataCriacao TIMESTAMP NOT NULL,
                    dataModificacao TIMESTAMP,
                    ultimaModificacaoPorPessoa INT REFERENCES pagini_pessoa(id) ON DELETE CASCADE
                )
                """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS pagini_link(
                    id SERIAL PRIMARY KEY,
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
                    Ciências Econômicas na UFSCar - Campus Sorocaba, com o intuito de conectar o desejo de ensinar com
                    a vontade de aprender. Para isso oferecemos semestralmente cursos de idioma, culturais e
                    administrativos, além de eventos, tudo isso de forma acessível e com certificado. Contamos com
                    professores voluntários e 7 áreas administrativas voluntárias dos quais ajudam a fazer o
                    projeto acontecer e crescer.</p><p class="par_news" align="justify">
                    <i>Essa notícia foi automaticamente criada pelo sistema.<br></i></p>""".trimIndent(), adminId)
            }

            if (it.createQuery("SELECT COUNT(id) FROM pagini_link").mapTo<Int>().one() < 1) {
                insertLink("Home", "/")
                insertLink("Login", "/login")
            }
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

    override fun getUsuarioByPessoa(pessoaId: Int): Usuario? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_usuario WHERE pessoaId = :pessoaId")
                .bind("pessoaId", pessoaId)
                .mapTo<Usuario>()
                .findOne()
                .orElse(null)
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

    override fun getLink(id: Int): Link? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM pagini_link WHERE id = :id")
                .bind("id", id)
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
        }.sortedBy { it.id }
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
            it.createUpdate("INSERT INTO pagini_noticia (titulo, html, criadoPorPessoa, dataCriacao) VALUES (:t, :html, :u, :d)")
                .bind("t", titulo)
                .bind("html", html)
                .bind("u", criadoPorPessoa)
                .bind("d", OffsetDateTime.now())
                .executeAndReturnGeneratedKeys()
                .mapTo<Int>()
                .one()
        }

        return Noticia(id, titulo, html, criadoPorPessoa)
    }

    override fun insertPagina(linkPagina: String, titulo: String, html: String, criadoPorPessoa: Int): Pagina {
        jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO pagini_pagina (linkPagina, titulo, html, criadoPorPessoa, dataCriacao) VALUES (:l, :t, :html, :u, :d)")
                .bind("l", linkPagina)
                .bind("t", titulo)
                .bind("html", html)
                .bind("u", criadoPorPessoa)
                .bind("d", OffsetDateTime.now())
                .execute()
        }

        return Pagina(linkPagina, titulo, html, criadoPorPessoa)
    }

    override fun insertLink(nome: String, href: String): Link {
        val id = jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO pagini_link (nome, href) VALUES (:nome, :href)")
                .bind("nome", nome)
                .bind("href", href)
                .executeAndReturnGeneratedKeys()
                .mapTo<Int>()
                .one()
        }

        return Link(id, nome, href)
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
            it.createUpdate("UPDATE pagini_link SET nome = :nome, href = :href WHERE id = :id")
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

    override fun removeLink(id: Int) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM pagini_link WHERE id = ?", id)
        }
    }

    override fun swapLinks(links: Pair<Link, Link>) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE pagini_link SET
                    nome = CASE id
                            WHEN :first.id THEN :second.nome
                            WHEN :second.id THEN :first.nome
                        END,
                    href = CASE id
                            WHEN :first.id THEN :second.href
                            WHEN :second.id THEN :first.href
                        END
                WHERE id in (:first.id, :second.id)
                """)
                .bindKotlin("first", links.first)
                .bindKotlin("second", links.second)
                .execute()
        }

        links.let { (first, second) ->
            val (_, tmpNome, tmpHref) = first
            first.nome = second.nome
            first.href = second.href
            second.nome = tmpNome
            second.href = tmpHref
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