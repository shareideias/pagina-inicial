package com.shareinstituto.view

import io.javalin.http.Context

interface View {
    fun render(ctx: Context)
}