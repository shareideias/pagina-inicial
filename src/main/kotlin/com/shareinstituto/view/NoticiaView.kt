package com.shareinstituto.view

import com.shareinstituto.model.Noticia
import com.shareinstituto.model.dao.DataAccessObject
import com.shareinstituto.view.base.PagIniView
import com.shareinstituto.view.base.PagIniView.Type.PUBLIC_PAGE
import io.javalin.http.Context
import kotlinx.html.*
import java.time.format.DateTimeFormatter

class NoticiaView(dao: DataAccessObject, private val noticia: Noticia) : PagIniView(dao, PUBLIC_PAGE) {
    override val pageTitle = noticia.titulo

    override fun MAIN.renderMain(ctx: Context) {
        div("container") {
            div("row") {
                div("col s12 xl8") {
                    article {
                        h3 { +noticia.titulo }
                        p("noticia_info") {
                            +DateTimeFormatter.RFC_1123_DATE_TIME.format(noticia.dataCriacao)
                            +" por "
                            +(dao.getPessoa(noticia.criadoPorPessoa)?.nome ?: "Usuário removido")
                        }
                        div("par_news") {
                            unsafe { +noticia.html }
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
}