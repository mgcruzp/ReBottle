package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class UserRoute(val path: String) {
    data object Inicio : UserRoute("user/inicio")
    data object Programar : UserRoute("user/programar")
    data object Recompensas : UserRoute("user/recompensas")
    data object Historial : UserRoute("user/historial")
    data object Perfil : UserRoute("user/perfil")
}

data class BottomItem(val route: UserRoute, val label: String, val icon: ImageVector)

@Composable
fun UserHome() {
    val nav = rememberNavController()
    val items = listOf(
        BottomItem(UserRoute.Historial, "Historial", Icons.Filled.History),
        BottomItem(UserRoute.Inicio,    "Inicio",    Icons.Filled.Home),
        BottomItem(UserRoute.Perfil,    "Perfil",    Icons.Filled.Person),
    )

    Scaffold(
        containerColor = Color.White,            // 👈 fondo del Scaffold en blanco
        contentColor   = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,    // 👈 fondo de la bottom bar en blanco
            ) {
                val current by nav.currentBackStackEntryAsState()
                val currentRoute = current?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route.path,
                        onClick = {
                            if (currentRoute != item.route.path) {
                                nav.navigate(item.route.path) {
                                    popUpTo(UserRoute.Inicio.path)
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        // 👇 contenedor del NavHost también en blanco
        NavHost(
            navController = nav,
            startDestination = UserRoute.Inicio.path,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)        // 👈 asegura blanco bajo cada pantalla
        ) {
            composable(UserRoute.Inicio.path) {
                InicioUsuarioScreen(
                    onProgramar = { nav.navigate(UserRoute.Programar.path) },
                    onRecompensas = { nav.navigate(UserRoute.Recompensas.path) }
                )
            }
            composable(UserRoute.Programar.path) { ProgramarSolicitudScreen() }
            composable(UserRoute.Recompensas.path) { RecompensasScreen() }
            composable(UserRoute.Historial.path) { HistorialScreen() }
            composable(UserRoute.Perfil.path) { PerfilScreen() }
        }
    }
}
