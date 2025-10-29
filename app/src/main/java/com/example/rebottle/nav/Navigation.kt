package com.example.rebottle.nav

//para recolector

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rebottle.ui.screens.recolector.ScannerScreen
import com.example.rebottle.ui.screens.recolector.PantallaRegistrarEntrega

enum class AppScreens {
    Scanner,
    PantallaRegistrarEntrega
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.PantallaRegistrarEntrega.name
    ) {
        composable(route = AppScreens.PantallaRegistrarEntrega.name) {
            PantallaRegistrarEntrega(navController = navController)
        }
        composable(route = AppScreens.Scanner.name) {
            ScannerScreen(navController = navController)
        }
    }
}