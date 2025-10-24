package com.example.rebottle.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.rebottle.domain.data.Role
import com.example.rebottle.model.UserAuthViewModel
import com.example.rebottle.ui.screens.log.InicioScreen
import com.example.rebottle.ui.screens.log.LoginScreen
import com.example.rebottle.ui.screens.log.RegisterScreen
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.rebottle.ui.screens.home.UserHome
import com.example.rebottle.ui.screens.home.RepHome
import com.example.rebottle.ui.screens.recolector.RecolectorHome
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    nav: NavHostController,
    viewModel: UserAuthViewModel
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Si hay usuario autenticado, empezar en Loading para verificar su rol
    val startDestination = if (currentUser != null) {
        Routes.Loading.path
    } else {
        Routes.Inicio.path
    }

    NavHost(navController = nav, startDestination = startDestination) {
        // VERIFICACIÓN DE SESIÓN
        composable(Routes.Loading.path) {
            LaunchedEffect(Unit) {
                viewModel.getCurrentUserRole { role ->
                    if (role != null) {
                        // Usuario autenticado con rol válido
                        nav.navigate(Routes.Home.create(role)) {
                            popUpTo(Routes.Loading.path) { inclusive = true }
                        }
                    } else {
                        // No hay usuario o rol inválido
                        nav.navigate(Routes.Inicio.path) {
                            popUpTo(Routes.Loading.path) { inclusive = true }
                        }
                    }
                }
            }

            // Mostrar indicador de carga
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // PANTALLA DE INICIO (SPLASH/PRINCIPIO)
        composable(Routes.Inicio.path) {
            InicioScreen(
                onStart = { nav.navigate(Routes.Login.path) }
            )
        }

        // LOGIN
        composable(Routes.Login.path) {
            LoginScreen(
                viewModel = viewModel, // 👈 Pasar ViewModel
                onLogin = { role ->
                    nav.navigate(Routes.Home.create(role)) {
                        popUpTo(Routes.Inicio.path) { inclusive = true }
                    }
                },
                onGoRegister = { nav.navigate(Routes.Register.path) }
            )
        }

        // REGISTRO
        composable(Routes.Register.path) {
            RegisterScreen(
                viewModel = viewModel, // 👈 Pasar ViewModel
                onRegistered = { role ->
                    nav.navigate(Routes.Home.create(role)) {
                        popUpTo(Routes.Inicio.path) { inclusive = true }
                    }
                },
                onGoLogin = { nav.popBackStack() }
            )
        }

        // HOME (ENRUTAMIENTO POR ROL)
        composable(
            route = Routes.Home.path,
            arguments = listOf(navArgument(Routes.Home.ARG_ROLE) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val roleStr = backStackEntry.arguments?.getString(Routes.Home.ARG_ROLE)
                ?: Role.USUARIO.name
            val role = runCatching { Role.valueOf(roleStr) }.getOrDefault(Role.USUARIO)

            // Función para cerrar sesión
            val onLogout: () -> Unit = {
                FirebaseAuth.getInstance().signOut()
                nav.navigate(Routes.Inicio.path) {
                    popUpTo(0) { inclusive = true }
                }
            }

            // Navegación según el rol
            when (role) {
                Role.USUARIO     -> UserHome(onLogout = onLogout)
                Role.RECOLECTOR  -> RecolectorHome(onLogout = onLogout)
                Role.EMPRESA_REP -> RepHome(onLogout = onLogout)
            }
        }
    }
}