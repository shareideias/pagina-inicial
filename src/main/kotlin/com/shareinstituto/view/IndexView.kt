package com.shareinstituto.view

import com.shareinstituto.model.Noticia
import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*
import org.jsoup.Jsoup
import java.time.format.DateTimeFormatter

class IndexView(val pageNumber: Int, val dao: DataAccessObject) : HtmlBuilderView() {
    override fun HTML.render(ctx: Context) {
        head {
            meta("viewport", "width=device-width, initial-scale=1.0")
            meta(charset = "utf-8")

            link("https://fonts.googleapis.com/icon?family=Material+Icons", "stylesheet")
            link("/css/materialize.min.css", "stylesheet") { media = "screen,projection" }
            link("/img/globo.png", "icon")
            link("/css/index.css", "stylesheet")

            title("Associação Share · Página Inicial")
        }
        body {
            header { header() }
            main { main() }
            footer(classes = "rodape_pag") { footer() }
            scripts()
        }
    }

    private fun HEADER.header() {
        nav(classes = "nav-wrapper transparent") {
            div(classes = "container") {
                a(href = "/", classes = "brand-logo") {
                    img(classes = "imagem_logo", alt = "Logo da Share", src = "/img/globo.png")
                }
                a(href = "", classes = "sidenav-trigger") {
                    attributes["data-target"] = "mobile-menu"
                    i(classes = "material-icons") { +"menu" }
                }

                val links = dao.allLinks()

                ul(classes = "right hide-on-med-and-down") {
                    for (link in links) {
                        li { a(classes = "link_menu", href = link.href) { +link.nome } }
                    }
                }
                ul(classes = "sidenav lighten-2") {
                    id = "mobile-menu"

                    for (link in links) {
                        li { a(href = link.href) { +link.nome } }
                    }
                }
            }
        }

        img(classes = "manuscrito_logo", alt = "Manuscrito da Share", src = "/img/share-fonte.png")
    }

    private fun DIV.renderCards(lastNoticias: List<Noticia>) {
        when (lastNoticias.size) {
            1 -> {
                div("col s12") { renderCard(lastNoticias[0]) }
            }
            2 -> {
                div("col s12 m6") { renderCard(lastNoticias[0]) }
                div("col s12 m6") { renderCard(lastNoticias[1]) }
            }
            3 -> {
                div("col s12 m6 xl4") { renderCard(lastNoticias[0]) }
                div("col s12 m6 xl4") { renderCard(lastNoticias[1]) }
                div("col s12 xl4") { renderCard(lastNoticias[2]) }
            }
        }
    }

    val compressRegex = Regex("\\s+")
    fun String.compressSpaces(): String {
        return replace(compressRegex, " ").trim()
    }

    fun String.limit(size: Int): String {
        return if (length <= size) this else substring(0, lastIndexOf(' ', size)).trim() + "..."
    }

    private fun DIV.renderCard(noticia: Noticia) {
        div("card") {
            div("card-content") {
                span("card-title") { +noticia.titulo }
                p("par_news") {
                    +Jsoup.parseBodyFragment(noticia.html, "/").text().compressSpaces().limit(280)
                }
            }
            div("card-action") { a("/n/${noticia.id}") { +"Leia mais" } }
        }
    }

    private fun MAIN.main() {
        div("container") {
            val lastNoticias = dao.paginateNoticias(0).take(3)
            if (lastNoticias.isNotEmpty()) {
                h5("underlined") { +"Recente:" }
                div("row") {
                    renderCards(lastNoticias)
                }
            }
            div("row") {
                div("col s12 xl8") {
                    h5("underlined") { +"Notícias:" }

                    val noticias = dao.paginateNoticias(pageNumber)
                    if (noticias.isNotEmpty()) {
                        for (noticia in noticias) {
                            article {
                                h3 { +noticia.titulo }
                                p("noticia_info") {
                                    a("/n/${noticia.id}") {
                                        +DateTimeFormatter.RFC_1123_DATE_TIME.format(noticia.dataCriacao)
                                        +" por "
                                        +(dao.getPessoa(noticia.criadoPorPessoa)?.nome ?: "Usuário removido")
                                    }
                                }
                                div("par_news") {
                                    unsafe { +noticia.html }
                                }
                            }
                        }
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

    private fun FOOTER.footer() {
        ul(classes = "redes_sociais") {
            li { a("https://www.facebook.com/shareideias/", classes = "facebook") { +"Facebook" } }
            li { a("https://www.instagram.com/shareideias/", classes = "instagram") { +"Instagram" } }
            li { a("https://www.linkedin.com/in/shareideias/", classes = "linkedin") { +"LinkedIn" } }
        }
        p { +"© Share. Todos os direitos reservados." }
    }

    private fun BODY.scripts() {
        script(src = "/js/materialize.min.js") {}
        script {
            unsafe {
                +"""
                    document.addEventListener('DOMContentLoaded', function() {
                        var elems = document.querySelectorAll('.sidenav');
                        var instances = M.Sidenav.init(elems, {});
                    });
                """.trimIndent()
            }
        }
    }
}