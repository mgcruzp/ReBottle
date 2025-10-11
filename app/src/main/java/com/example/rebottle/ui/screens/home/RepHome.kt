package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rebottle.R
import com.example.rebottle.nav.RepRoute
import kotlinx.coroutines.launch

/* ====== Colores ====== */
private val PillGreen    = Color(0xFFB8F8AD)
private val TextIcon     = Color(0xFF1B4332)   // títulos/íconos
private val BgSoft       = Color(0xFFF7F7F7)   // fondo “un poco blanco”
private val CardWhite    = Color(0xFFFFFFFF)   // botones/targets blancos

/* =====================  NAV + BOTTOM BAR  ===================== */

@Composable
fun RepHome() {
    val innerNav = rememberNavController()

    Scaffold(
        containerColor = BgSoft,
        contentColor   = MaterialTheme.colorScheme.onBackground,
        bottomBar = { RepBottomBar(innerNav) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BgSoft)
        ) {
            NavHost(
                navController = innerNav,
                startDestination = RepRoute.Inicio.path
            ) {
                composable(RepRoute.Inicio.path) {
                    RepInicioScreen(
                        onGoReportes = { innerNav.navigate(RepRoute.Reportes.path) },
                        onGoCumplimiento = { innerNav.navigate(RepRoute.Cumplimiento.path) }
                    )
                }
                composable(RepRoute.Reportes.path)     { RepReportesScreen() }
                composable(RepRoute.Cumplimiento.path) { RepCumplimientoScreen() }
                composable(RepRoute.Perfil.path)       { RepPerfilScreen() }
            }
        }
    }
}

@Composable
private fun RepBottomBar(nav: NavHostController) {
    val current by nav.currentBackStackEntryAsState()
    val currentRoute = current?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        val items = listOf(
            Triple("Inicio", Icons.Outlined.Home, RepRoute.Inicio.path),
            Triple("Reportes", Icons.Outlined.Assignment, RepRoute.Reportes.path),
            Triple("Cumplimiento", Icons.Outlined.BarChart, RepRoute.Cumplimiento.path),
            Triple("Perfil", Icons.Outlined.Business, RepRoute.Perfil.path)
        )

        items.forEach { (label, icon, route) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        nav.navigate(route) {
                            popUpTo(RepRoute.Inicio.path)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = TextIcon,
                    unselectedIconColor = TextIcon.copy(alpha = 0.55f),
                    selectedTextColor   = TextIcon,
                    unselectedTextColor = TextIcon.copy(alpha = 0.55f),
                    indicatorColor      = PillGreen
                )
            )
        }
    }
}

/* =====================  INICIO (centrado + 2 tarjetas)  ===================== */

@Composable
private fun RepInicioScreen(
    onGoReportes: () -> Unit,
    onGoCumplimiento: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(BgSoft)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título centrado
        Text(
            text = "Hola Empresa!",
            color = TextIcon,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Logo Rebottle centrado
        Spacer(Modifier.height(6.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_rebottle),
            contentDescription = "Rebottle",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(80.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Tarjeta 1: Reportes Certificados (blanca)
        InicioCardBig(
            title = "Reportes Certificados",
            imageRes = R.drawable.reportes_certificados,
            onClick = onGoReportes
        )

        Spacer(Modifier.height(14.dp))

        // Tarjeta 2: Cumplimiento REP (blanca)
        InicioCardBig(
            title = "Cumplimiento REP",
            imageRes = R.drawable.cumplimiento_rep,
            onClick = onGoCumplimiento
        )
    }
}

@Composable
private fun InicioCardBig(
    title: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(containerColor = CardWhite),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp) // más alto para que NO se corte el título
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth(0.48f)
                    .aspectRatio(1.2f)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = title,
                color = TextIcon,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/* =====================  REPORTES CERTIFICADOS (2 ítems)  ===================== */

private data class CertReport(
    val fecha: String,
    val periodo: String,
    val toneladas: String,
    val archivo: String
)

@Composable
private fun RepReportesScreen() {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val items = remember {
        listOf(
            CertReport("12/09/2025", "Enero–Agosto 2025", "18.2 t", "Certificado_REP_2025-08.pdf"),
            CertReport("15/07/2025", "Enero–Junio 2025",  "12.0 t", "Certificado_REP_2025-06.pdf")
        )
    }

    Scaffold(
        containerColor = BgSoft,
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BgSoft)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Reportes Certificados",
                    color = TextIcon,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(Modifier.height(10.dp))

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.certificado),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(160.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            items(items) { r ->
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = CardWhite),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Fecha: ${r.fecha}", fontWeight = FontWeight.SemiBold, color = TextIcon)
                        Text("Periodo: ${r.periodo}", color = TextIcon.copy(alpha = 0.9f))
                        Text("Total valorizado: ${r.toneladas}", color = TextIcon.copy(alpha = 0.9f))
                        Spacer(Modifier.height(8.dp))
                        FilledTonalButton(
                            onClick = { scope.launch { snackbar.showSnackbar("Descarga simulada: ${r.archivo}") } }
                        ) {
                            Icon(Icons.Outlined.Download, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Descargar")
                        }
                    }
                }
            }
        }
    }
}

/* =====================  CUMPLIMIENTO REP  ===================== */

private data class MaterialMetric(
    val nombre: String,
    var porcentaje: Float
)

@Composable
private fun RepCumplimientoScreen() {
    var metaAnual by rememberSaveable { mutableFloatStateOf(0.30f) }
    var avanceActual by rememberSaveable { mutableFloatStateOf(0.18f) }

    val materiales = remember {
        mutableStateListOf(
            MaterialMetric("PET", 0.18f),
            MaterialMetric("HDPE", 0.10f),
            MaterialMetric("Cartón", 0.05f),
            MaterialMetric("Vidrio", 0.01f),
            MaterialMetric("Metales", 0.03f)
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(BgSoft)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 👇 Se eliminó decor_top (“alas”)
        Text(
            "Cumplimiento REP",
            color = TextIcon,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = CardWhite)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Meta anual: ${(metaAnual * 100).toInt()}%", color = TextIcon)
                Slider(value = metaAnual, onValueChange = { metaAnual = it }, steps = 98)

                Text("Avance actual: ${(avanceActual * 100).toInt()}%", color = TextIcon)
                Slider(value = avanceActual, onValueChange = { avanceActual = it }, steps = 98)

                LinearProgressIndicator(
                    progress = { (avanceActual / metaAnual.coerceAtLeast(0.01f)).coerceAtMost(1f) },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Progreso vs meta: ${((avanceActual / metaAnual).coerceAtMost(1f) * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextIcon
                )
            }
        }

        Text("Materiales", style = MaterialTheme.typography.titleMedium, color = TextIcon)

        materiales.forEach { m ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = CardWhite)) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(m.nombre, fontWeight = FontWeight.SemiBold, color = TextIcon)
                        Text("${(m.porcentaje * 100).toInt()}%", color = TextIcon)
                    }
                    Spacer(Modifier.height(6.dp))
                    Slider(value = m.porcentaje, onValueChange = { m.porcentaje = it }, steps = 98)
                    LinearProgressIndicator(progress = { m.porcentaje })
                }
            }
        }
    }
}

/* =====================  PERFIL  ===================== */

@Composable
private fun RepPerfilScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(BgSoft)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Outlined.Apartment, contentDescription = null, modifier = Modifier.size(96.dp), tint = TextIcon)
        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = CardWhite)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = "Nombre Empresa", onValueChange = {}, label = { Text("Nombre") })
                OutlinedTextField(value = "rep@empresa.com", onValueChange = {}, label = { Text("Correo") })
                Button(onClick = { }) { Text("Editar perfil") }
            }
        }
    }
}
