package com.shareinstituto.view

import com.shareinstituto.model.Noticia
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.utils.compressSpaces
import com.shareinstituto.utils.limit
import com.shareinstituto.view.base.PagIniView
import com.shareinstituto.view.base.PagIniView.Type.INDEX
import io.javalin.http.Context
import kotlinx.html.*
import org.jsoup.Jsoup
import java.time.format.DateTimeFormatter

class IndexView(val pageNumber: Int, dao: DataAccessObject) : PagIniView(dao, INDEX) {
    override val pageTitle = "Página Inicial"

    override fun MAIN.renderMain(ctx: Context) {
        div("container") {
            dao.paginateNoticias(0).take(3).takeIf { it.isNotEmpty() }?.let { renderCards(it) }
            div("row") {
                div("col s12 xl8") {
                    h5("underlined") { +"Notícias:" }

                    val noticias = dao.paginateNoticias(pageNumber)
                    if (noticias.isNotEmpty()) {
                        renderNoticias(noticias)
                    } else {
                        p("blue-grey-text lighten-3 center-align") {
                            i {
                                if (pageNumber == 0) {
                                    +"O site não tem nenhuma notícia. Elas serão mostradas aqui."
                                } else {
                                    +"Essa página não contém notícias. Por favor, "
                                    a("/") { +"volte a página inicial" }
                                    +"."
                                }
                            }
                        }
                    }
                }
                div("col s12 xl4") {
                    h5("underlined") { +"Sobre nós:" }
                    p("par_news") {
                        +"A Share é uma Entidade Estudantil fundada em 2016 por alunos de Ciências Econômicas na"
                        +" UFSCar - Campus Sorocaba, com o intuito de conectar a vontade de ensinar com a vontade de aprender."
                        +" Para isso oferecemos semestralmente cursos de idioma, culturais e administrativos, além de eventos,"
                        +" tudo isso de forma acessível e com certificado."
                        +" Contamos com professores voluntários e 7 áreas administrativas voluntárias"
                        +" dos quais ajudam a fazer o projeto acontecer e crescer."
                    }
                }
            }
        }
    }

    private fun DIV.renderCards(noticias: List<Noticia>) {
        h5("underlined") { +"Recente:" }
        div("row") {
            when (noticias.size) {
                1 -> {
                    div("col s12") { renderCard(noticias[0]) }
                }
                2 -> {
                    div("col s12 m6") { renderCard(noticias[0]) }
                    div("col s12 m6") { renderCard(noticias[1]) }
                }
                3 -> {
                    div("col s12 m6 xl4") { renderCard(noticias[0]) }
                    div("col s12 m6 xl4") { renderCard(noticias[1]) }
                    div("col s12 xl4") { renderCard(noticias[2]) }
                }
            }
        }
    }

    private fun DIV.renderCard(noticia: Noticia) {
        div("card") {
            div("card-content") {
                span("card-title") { +noticia.titulo }
                p("justify") {
                    +Jsoup.parseBodyFragment(noticia.html, "/").text().compressSpaces().limit(280)
                }
            }
            div("card-action") { a("/n/${noticia.id}") { +"Leia mais" } }
        }
    }

    private fun DIV.renderNoticias(noticias: List<Noticia>) {
        noticias.forEach {
            article {
                h3 { +it.titulo }
                p("article-info") {
                    a("/n/${it.id}") {
                        +DateTimeFormatter.RFC_1123_DATE_TIME.format(it.dataCriacao)
                        +" por "
                        +(dao.getPessoa(it.criadoPorPessoa)?.nome ?: "Usuário removido")
                    }
                }
                div("justify") { unsafe { +it.html } }
            }
        }
    }
}