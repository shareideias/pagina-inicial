package com.shareinstituto.view

import com.shareinstituto.model.dao.DataAccessObject
import io.javalin.http.Context
import kotlinx.html.*

class AdminView(dao: DataAccessObject) : ModeloView(dao) {
    override val pageTitle = "Administração"
    override val extraCss = listOf("/css/administração.css")
    override val mainPage = "/admin"

    override fun MAIN.main(ctx: Context) {
        h2("Administração")

        div(classes = "container") {
            div(classes = "row") {
                div(classes = "col 14 m6 s12") {
                    h3(classes = "news_title") {
                        +"Recentes"
                    }
                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"short_text"
                            }
                            span(classes = "title") {
                                +"Recente 1"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"short_text"
                            }
                            span(classes = "title") {
                                +"Recente 2"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"short_text"
                            }
                            span(classes = "title") {
                                +"Recente 3"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    h3(classes = "news_title") {
                        +"Notícias"
                    }
                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 1"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 2"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 3"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }


                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"receipt"
                            }
                            span(classes = "title") {
                                +"Notícia 4"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }
                }

                div(classes = "col 14 m6 s12") {
                    h3(classes = "news_title") {
                        +"Páginas"
                    }
                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
                            }
                            span(classes = "title") {
                                +"Sobre nós"
                            }
                            p {
                                +"A Share - Centro de Línguas é uma entidade estudantil da UFSCar - Campus Sorocaba que surgiu"
                                +" no ano de 2017, com o intuito de disponibilizar o ensino de línguas para os alunos da"
                                +" universidade. Atualmente, além de oferecermos diversos cursos de línguas ainda atuamos em outras"
                                +" áreas (como dança e fotografia) e visamos sempre atingir novos objetivos."
                            }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
                            }
                            span(classes = "title") {
                                +"Institucional"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
                            }
                            span(classes = "title") {
                                +"Cursos"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                    ul(classes = "collection") {
                        li(classes = "collection-item avatar") {
                            i(classes = "material-icons circle") {
                                +"folder"
                            }
                            span(classes = "title") {
                                +"Inscrições"
                            }
                            p { +"Text" }
                            a(href = "#!", classes = "secondary-content") {
                                i(classes = "material-icons") {
                                    +"edit"
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}