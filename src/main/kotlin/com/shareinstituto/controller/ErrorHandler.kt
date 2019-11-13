package com.shareinstituto.controller

import com.shareinstituto.controller.security.ForbiddenAccessException
import com.shareinstituto.controller.security.NotFoundException
import com.shareinstituto.controller.security.UnableToEditException
import com.shareinstituto.model.page.ErrorViewModel
import com.shareinstituto.view.ErrorView
import io.javalin.Javalin
import org.eclipse.jetty.http.HttpStatus
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class ErrorHandler(override val kodein: Kodein) : KodeinAware {
    fun Javalin.addErrorHandlers() {
        exception(NotFoundException::class.java) { e, ctx ->
            ErrorView(ErrorViewModel(
                e.isAdmin,
                HttpStatus.NOT_FOUND_404,
                "Não Encontrado",
                "Você está tentando acessar ${e.type.what} que não existe mais."
            )).render(ctx)
        }

        exception(UnableToEditException::class.java) { e, ctx ->
            ErrorView(ErrorViewModel(
                e.isAdmin,
                HttpStatus.PRECONDITION_FAILED_412,
                "Falha na Edição",
                "Você tentou editar ${e.type.what} que não existe. Ao menos, não mais."
            )).render(ctx)
        }

        exception(ForbiddenAccessException::class.java) { _, ctx ->
            ErrorView(ErrorViewModel(
                false,
                HttpStatus.FORBIDDEN_403,
                "Acesso Negado",
                "Você está tentando acessar uma página que você não pode acessar."
            )).render(ctx)
        }

        error(HttpStatus.FORBIDDEN_403) { ctx ->
            ErrorView(ErrorViewModel(
                false,
                HttpStatus.FORBIDDEN_403,
                "Acesso Negado",
                "Você está tentando acessar uma página que você não pode acessar."
            )).render(ctx)
        }
        error(HttpStatus.NOT_FOUND_404) { ctx ->
            ErrorView(ErrorViewModel(
                false,
                HttpStatus.NOT_FOUND_404,
                "Não Encontrado",
                "Você está tentando acessar algo que não existe mais."
            )).render(ctx)
        }
        error(HttpStatus.INTERNAL_SERVER_ERROR_500) { ctx ->
            ErrorView(ErrorViewModel(
                false,
                HttpStatus.INTERNAL_SERVER_ERROR_500,
                "Erro Interno no Servidor",
                "O servidor sofreu uma falha enquanto processava a página. Pedimos desculpas."
            )).render(ctx)
        }
    }
}