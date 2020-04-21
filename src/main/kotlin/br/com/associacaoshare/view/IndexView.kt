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
        div(classes = "janela") {
            id = "home"
            div(classes = "conteudo-janela") {
                a(classes = "fechar") {
                    href = "#home"
                    +"""X"""
                }
                h2(classes = "titulo-janela") { +"""Processo seletivo de alunos""" }
                p { +"""Está interessado em nossos cursos?""" }
                p { +"""As inscrições vão do dia 25/04 até 28/04.""" }

                /*p(classes = "datas-janela") { +"""de 28/02 até 14/03:""" }*/
                div(classes = "botaoadm") {
                    a {
                        href = "https://bit.ly/edital-aluno"
                        button(classes = "col s12 btn btn-large waves-effect") { +"""Edital""" }
                    }
                    br {
                    }
                }

                /*p(classes = "datas-janela") { +"""de 28/02 até 14/03:""" }
                div(classes = "botaoprof") {
                    a {
                        href = "http://bit.ly/InscriçõesProf"
                        button(classes = "col s12 btn btn-large waves-effect") { +"""Professor""" }
                    }
                    br {
                    }
                }*/

                p(classes = "datas-janela") /*{ +"""de 25/04 até 28/04:""" }*/
                div(classes = "botaoaluno") {
                    a {
                        href = "https://forms.gle/pK4RXSeFdfpzqcty8"
                        button(classes = "col s12 btn btn-large waves-effect") { +"""Inscreva-se""" }
                    }
                    br {
                    }
                }
            }
        }
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
                p("compartilhamento-titulo"){+"Compartilhe:"}
                ul("social-sharing") {
                    li { a("https://www.facebook.com/sharer/sharer.php?u=https://associacaoshare.com.br/n/${it.id}", classes = "img-fb") { +"Facebook" } }
                    li { a("https://api.whatsapp.com/send?text=https://associacaoshare.com.br/n/${it.id}", classes = "img-wpp") { +"Whatsapp" } }
                    li { a("https://www.linkedin.com/shareArticle?mini=true&url=https://associacaoshare.com.br/n/${it.id}", classes = "img-ln") { +"LinkedIn" } }
                }
            }
        }
    }
}