package com.shareinstituto.view.base

import io.javalin.http.Context

interface View {
    fun render(ctx: Context)
}