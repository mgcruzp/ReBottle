package com.example.rebottle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.rebottle.nav.NavGraph
import com.example.rebottle.ui.theme.RebottleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RebottleTheme {
                // Controlador de navegación
                val navController = rememberNavController()

                // Contenedor base con colores del tema
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Gráfico de navegación
                    NavGraph(nav = navController)
                }
            }
        }
    }
}
