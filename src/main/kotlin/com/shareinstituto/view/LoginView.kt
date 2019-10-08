package com.shareinstituto.view

import com.shareinstituto.model.dao.MainDao
import io.javalin.http.Context
import kotlinx.html.*

class LoginView(dao: MainDao) : ModeloView(dao) {
    override val pageTitle = "Login"
    override val extraCss = listOf("/css/login.css")
    override val mainPage = "/"

    override fun MAIN.main(ctx: Context) {
        div(classes = "container caixa_login") {
            div(classes = "z-depth-1 grey lighten-4 row geral_login") {
                img(classes = "imagem_login", alt = "Logo da Share", src = "/img/share-logo.png")
                form(classes = "col s12", method = FormMethod.post, action = "/doLogin") {

                    ctx.queryParam("err")?.let { err ->
                        div("card-panel red darken-1 white-text center-align") {
                            when (err) {
                                "invalidcreds" -> {
                                    +"Credenciais inválidas."
                                }
                                "unauthorized" -> {
                                    +"Por favor, faça login."
                                }
                                else -> {
                                    +"Erro desconhecido."
                                }
                            }
                        }
                    }

                    ctx.queryParam("then")?.let { then -> hiddenInput(name = "then") { value = then } }

                    div(classes = "input-field") {
                        label {
                            htmlFor = "username"
                            +"Nome de usuário"
                        }
                        input(classes = "validate", type = InputType.text, name = "username") {
                            id = "username"
                            ctx.queryParam("username")?.let { value = it }
                        }
                    }

                    div(classes = "input-field") {
                        label {
                            htmlFor = "password"
                            +"Senha"
                        }
                        input(classes = "validate", type = InputType.password, name = "password") {
                            id = "password"
                        }
                    }

                    p("center-align") {
                        label {
                            checkBoxInput(name = "ilovecookies")
                            span { +"Lembrar-me" }
                        }
                    }

                    button(type = ButtonType.submit, name = "btn_login", classes = "col s12 btn btn-large waves-effect light-blue lighten-2") {
                        +"Entrar"
                    }
                }
            }
        }

        div(classes = "section")
        div(classes = "section")
    }
}