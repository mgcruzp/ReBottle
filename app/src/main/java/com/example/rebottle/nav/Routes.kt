package com.example.rebottle.nav

import com.example.rebottle.domain.data.Role

// Rutas principales (inicio/login/register/home)
sealed class Routes(val path: String) {
    data object Inicio   : Routes("inicio")
    data object Login    : Routes("login")
    data object Register : Routes("register")
    data object Loading  : Routes("loading")

    // Home recibe el rol para enrutar al flujo correspondiente
    data object Home : Routes("home/{role}") {
        fun create(role: Role) = "home/${role.name}"
        const val ARG_ROLE = "role"
    }
}

// Rutas internas del usuario
sealed class UserRoute(val path: String) {
    data object Inicio      : UserRoute("user/inicio")
    data object Programar   : UserRoute("user/programar")
    data object Recompensas : UserRoute("user/recompensas")
    data object Historial   : UserRoute("user/historial")
    data object Perfil      : UserRoute("user/perfil")
    data object Solicitudes : UserRoute("user/solicitudes")
}

// Rutas internas Empresa REP
sealed class RepRoute(val path: String) {
    data object Inicio        : RepRoute("rep/inicio")
    data object Reportes      : RepRoute("rep/reportes")
    data object Cumplimiento  : RepRoute("rep/cumplimiento")
    data object Perfil        : RepRoute("rep/perfil")
}

// Rutas internas del Recolector
sealed class CollectorRoute(val path: String) {
    data object Inicio   : CollectorRoute("collector/inicio")
    data object Registro : CollectorRoute("collector/registro")
    data object Mapa   : CollectorRoute("collector/mapa")
    data object Perfil  : CollectorRoute("collector/perfil")
    data object PendingRequests : CollectorRoute("collector/pendingRequests")

}
