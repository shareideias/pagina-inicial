package com.shareinstituto.view.base

import com.shareinstituto.model.page.PagIniAdminModel
import com.shareinstituto.view.base.PagIniView.Type.ADMIN_PAGE

abstract class PagIniAdminView : PagIniView(ADMIN_PAGE) {
    override val model: PagIniAdminModel = PagIniAdminModel
    override val mainPage = "/admin"
}