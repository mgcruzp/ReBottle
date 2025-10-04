package com.example.rebottle.nav

import com.example.rebottle.domain.data.Role

// Rutas principales (inicio/login/register/home)
sealed class Routes(val path: String) {
    data object Inicio   : Routes("inicio")
    data object Login    : Routes("login")
    data object Register : Routes("register")

    // Home recibe el rol para enrutar al flujo correspondiente
    data object Home : Routes("home/{role}") {
        fun create(role: Role) = "home/${role.name}"
        const val ARG_ROLE = "role"
    }
}

sealed class UserRoute(val path: String) {
    data object Inicio      : UserRoute("user/inicio")
    data object Programar   : UserRoute("user/programar")
    data object Recompensas : UserRoute("user/recompensas")
    data object Historial   : UserRoute("user/historial")
    data object Perfil      : UserRoute("user/perfil")
}
