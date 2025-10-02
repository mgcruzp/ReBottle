package com.example.rebottle.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.rebottle.domain.data.Role
import com.example.rebottle.ui.screens.log.InicioScreen
import com.example.rebottle.ui.screens.log.LoginScreen
import com.example.rebottle.ui.screens.log.RegisterScreen
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rebottle.ui.screens.home.UserHome

@Composable
fun NavGraph(nav: NavHostController) {
    NavHost(navController = nav, startDestination = Routes.Inicio.path) {

        composable(Routes.Inicio.path) {
            InicioScreen(
                onStart = { nav.navigate(Routes.Login.path) }
            )
        }

        composable(Routes.Login.path) {
            LoginScreen(
                onLogin = { role ->
                    nav.navigate(Routes.Home.create(role)) {
                        popUpTo(Routes.Inicio.path) { inclusive = false }
                    }
                },
                onGoRegister = { nav.navigate(Routes.Register.path) }
            )
        }

        composable(Routes.Register.path) {
            RegisterScreen(
                onRegistered = { role ->
                    nav.navigate(Routes.Home.create(role)) {
                        popUpTo(Routes.Login.path) { inclusive = true }
                    }
                },
                onGoLogin = { nav.navigate(Routes.Login.path) }
            )
        }

        composable(
            route = Routes.Home.path,
            arguments = listOf(navArgument(Routes.Home.ARG_ROLE) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val roleStr = backStackEntry.arguments?.getString(Routes.Home.ARG_ROLE) ?: Role.USUARIO.name
            val role = runCatching { Role.valueOf(roleStr) }.getOrDefault(Role.USUARIO)

            when (role) {
                Role.USUARIO -> UserHome()
                Role.RECOLECTOR -> Surface { Text("Home Recolector — por construir") }
                Role.EMPRESA_REP -> Surface { Text("Home Empresa REP — por construir") }
            }
        }

    }
}

/** Placeholder simple para Home por rol (todo en un solo archivo). */
@Composable
private fun HomeScreen(role: Role) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Rebottle", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(12.dp))
            Text("Has iniciado como: ${role.displayName}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))
            Text(
                when (role) {
                    Role.USUARIO -> "Aquí irá: crear solicitud, ver estado y historial."
                    Role.RECOLECTOR -> "Aquí irá: rutas asignadas, marcar recogido, evidencias."
                    Role.EMPRESA_REP -> "Aquí irá: reportes, trazabilidad, exportaciones."
                }
            )
        }
    }
}
