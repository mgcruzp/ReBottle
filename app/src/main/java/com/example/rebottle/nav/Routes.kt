package com.example.rebottle.nav

import com.example.rebottle.domain.data.Role

sealed class Routes(val path: String) {
    data object Inicio : Routes("inicio")
    data object Login : Routes("login")
    data object Register : Routes("register")

    data object Home : Routes("home/{role}") {
        fun create(role: Role) = "home/${role.name}"
        const val ARG_ROLE = "role"
    }
}
