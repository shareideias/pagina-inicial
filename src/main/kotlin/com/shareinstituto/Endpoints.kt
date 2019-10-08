package com.shareinstituto

import com.shareinstituto.controller.AdminController
import com.shareinstituto.controller.LoginController
import com.shareinstituto.controller.PublicController
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.EndpointGroup
import org.kodein.di.Kodein

class Endpoints(val kodein: Kodein) : EndpointGroup {
    override fun addEndpoints() {
        PublicController(kodein).addEndpoints()
        LoginController(kodein).addEndpoints()
        path("admin") { AdminController(kodein).addEndpoints() }
    }
}

