package com.example.rebottle.ui.screens.recolector

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rebottle.model.LocationViewModel
import com.example.rebottle.nav.CollectorRoute
import com.example.rebottle.nav.UserRoute
import com.example.rebottle.ui.screens.PantallaRecolector
import com.example.rebottle.ui.screens.recolector.PerfilScreenR

data class BottomItem(
    val route: CollectorRoute,
    val label: String,
    val icon: ImageVector
)
@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun RecolectorHome(onLogout: () -> Unit) {
    val nav = rememberNavController()
    val vm = LocationViewModel()
    val items = listOf(
        BottomItem(CollectorRoute.Inicio, "Inicio", Icons.Filled.Home),
        BottomItem(CollectorRoute.Registro, "Registrar", Icons.Filled.AddCircle),
        BottomItem(CollectorRoute.Mapa, "Mapa", Icons.Filled.Map),
        BottomItem(CollectorRoute.Perfil, "Perfil", Icons.Filled.Person)
        )


    val PillGreen = Color(0xFFB8F8AD)
    val TextIcon = Color(0xFF1B4332)

    Scaffold(
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.onBackground,
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
                                    popUpTo(CollectorRoute.Inicio.path)
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TextIcon,
                            unselectedIconColor = TextIcon.copy(alpha = 0.55f),
                            selectedTextColor = TextIcon,
                            unselectedTextColor = TextIcon.copy(alpha = 0.55f),
                            indicatorColor = PillGreen
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = CollectorRoute.Inicio.path,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            composable(CollectorRoute.Inicio.path) {
                PantallaRecolector()
            }
            composable(CollectorRoute.Registro.path) {
                PantallaRegistrarEntrega()
            }
            composable(CollectorRoute.Mapa.path) {
                LocationScreen(vm)
            }
            composable(CollectorRoute.Perfil.path) {
                PerfilScreenR(navController = nav,
                    onLogout = { onLogout() } )
            }
        }
    }
}
