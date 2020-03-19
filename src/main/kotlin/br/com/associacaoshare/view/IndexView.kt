package br.com.associacaoshare.view

import br.com.associacaoshare.model.Noticia
import br.com.associacaoshare.model.page.IndexViewModel
import br.com.associacaoshare.utils.compressSpaces
import br.com.associacaoshare.utils.limit
import br.com.associacaoshare.view.base.PagIniView
import br.com.associacaoshare.view.base.PagIniView.Type.INDEX
import io.javalin.http.Context
import kotlinx.html.*
import org.jsoup.Jsoup
import java.time.format.DateTimeFormatter

class IndexView(override val model: IndexViewModel) : PagIniView(INDEX) {
    override val pageTitle = "Página Inicial"

    override fun MAIN.renderMain(ctx: Context) {


        /*Parte do código temporária para a divulgação dos processos seletivos*/
        /*Só deverá ser ativada ao começo de cada semestre*/
        /*div(classes = "janela") {
            id = "home"
            div(classes = "conteudo-janela") {
                a(classes = "fechar") {
                    href = "#home"
                    +"""X"""
                }
                h2(classes = "titulo-janela") { +"""Processo seletivo""" }
                p { +"""Está interessado em fazer parte da história de nossa entidade?""" }
                p { +"""Se inscreva em um ou mais de nossos processos:""" }

                p(classes = "datas-janela") { +"""de 28/02 até 14/03:""" }
                div(classes = "botaoadm") {
                    a {
                        href = "http://bit.ly/InscriçõesAdm"
                        button(classes = "col s12 btn btn-large waves-effect") { +"""Administrativo""" }
                    }
                    br {
                    }
                }

                p(classes = "datas-janela") { +"""de 28/02 até 14/03:""" }
                div(classes = "botaoprof") {
                    a {
                        href = "http://bit.ly/InscriçõesProf"
                        button(classes = "col s12 btn btn-large waves-effect") { +"""Professor""" }
                    }
                    br {
                    }
                }

                p(classes = "datas-janela") { +"""de 26/03 até 30/03:""" }
                div(classes = "botaoaluno") {
                    a {
                        href = ""
                        button(classes = "col s12 btn btn-large waves-effect") { +"""Aluno""" }
                    }
                    br {
                    }
                }
            }
        }*/
        /*fim da parte temporária*/


        div("container") {
            model.cards.takeIf { it.isNotEmpty() }?.let { renderCards(it) }
            div("row") {
                div("col s12 xl8") {
                    h5("underlined") { +"Notícias:" }

                    if (model.noticias.isNotEmpty()) {
                        renderNoticias(model.noticias)
                    } else {
                        p("blue-grey-text lighten-3 center-align") {
                            i {
                                if (model.pageNumber == 0) {
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
                        +" UFSCar - Campus Sorocaba, com o intuito de conectar o desejo de ensinar com a vontade de aprender."
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
                        +(model.pessoas[it.criadoPorPessoa]?.nome ?: "Usuário removido")
                    }
                }
                div("justify") { unsafe { +it.html } }
            }
        }
    }
}