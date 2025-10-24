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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rebottle.nav.UserRoute

data class BottomItem(val route: UserRoute, val label: String, val icon: ImageVector)

@Composable
fun UserHome(onLogout: () -> Unit) {
    val nav = rememberNavController()
    val items = listOf(
        BottomItem(UserRoute.Historial, "Historial", Icons.Filled.History),
        BottomItem(UserRoute.Inicio,    "Inicio",    Icons.Filled.Home),
        BottomItem(UserRoute.Perfil,    "Perfil",    Icons.Filled.Person),
    )

    val PillGreen = Color(0xFFB8F8AD) // color de la “pastilla” seleccionada
    val TextIcon  = Color(0xFF1B4332) // color de íconos y textos

    Scaffold(
        containerColor = Color.White,
        contentColor   = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
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
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = TextIcon,
                            unselectedIconColor = TextIcon.copy(alpha = 0.55f),
                            selectedTextColor   = TextIcon,
                            unselectedTextColor = TextIcon.copy(alpha = 0.55f),
                            indicatorColor      = PillGreen // ← adiós morado, hola verde
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = UserRoute.Inicio.path,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            composable(UserRoute.Inicio.path) {
                InicioUsuarioScreen(
                    onProgramar   = { nav.navigate(UserRoute.Programar.path) },
                    onRecompensas = { nav.navigate(UserRoute.Recompensas.path) }
                )
            }
            composable(UserRoute.Programar.path)   { ProgramarSolicitudScreen() }
            composable(UserRoute.Recompensas.path) { RecompensasScreen() }
            composable(UserRoute.Historial.path)   { HistorialScreen() }
            composable(UserRoute.Perfil.path)      { PerfilScreen() }
        }
    }
}
