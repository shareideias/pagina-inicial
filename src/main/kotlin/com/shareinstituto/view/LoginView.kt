package com.shareinstituto.view

import com.shareinstituto.model.dao.MainDao
import kotlinx.html.*

class LoginView(dao: MainDao) : ModeloView(dao) {
    override val pageTitle = "Login"
    override val extraCss = listOf("/css/login.css")

    override fun MAIN.main() {
        div(classes = "container caixa_login") {
            div(classes = "z-depth-1 grey lighten-4 row geral_login") {
                img(classes = "imagem_login", alt = "Logo da Share", src = "/img/share-logo.png")
                form(classes = "col s12", method = FormMethod.post) {
                    div(classes = "row") {
                        div(classes = "input-field col s12") {
                            label {
                                htmlFor = "username"
                                +"Nome de usuário"
                            }
                            input(classes = "validate", type = InputType.text, name = "username") {
                                id = "username"
                            }
                        }
                    }

                    div(classes = "row") {
                        div(classes = "input-field col s12") {
                            label {
                                htmlFor = "password"
                                +"Senha"
                            }
                            input(classes = "validate", type = InputType.password, name = "password") {
                                id = "password"
                            }
                        }
                    }

                    div(classes = "row") {
                        button(type = ButtonType.submit, name = "btn_login", classes = "col s12 btn btn-large waves-effect botao_enviar") {
                            +"Entrar"
                        }
                    }
                }
            }
        }

        div(classes = "section")
        div(classes = "section")
    }
}