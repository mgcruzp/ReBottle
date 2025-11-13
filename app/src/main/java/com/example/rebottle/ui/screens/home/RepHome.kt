package com.example.rebottle.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rebottle.R
import com.example.rebottle.nav.RepRoute
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

/* ====== Colores ====== */
private val PillGreen    = Color(0xFFB8F8AD)
private val TextIcon     = Color(0xFF1B4332)   // títulos/íconos
private val BgSoft       = Color(0xFFF7F7F7)   // fondo “un poco blanco”
private val CardWhite    = Color(0xFFFFFFFF)   // botones/targets blancos

/* =====================  NAV + BOTTOM BAR  ===================== */

@Composable
fun RepHome(onLogout: () -> Unit) {
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
                composable(RepRoute.Perfil.path) { PerfilScreene( onLogout = { onLogout() } )
                }
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

        Spacer(Modifier.height(6.dp))

        Image(

                    painter = painterResource(R.drawable.logo_rebottle),
            contentDescription = "Rebottle",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(200.dp)
        )

        Spacer(Modifier.height(16.dp))

        InicioCardBig(
            title = "Reportes Certificados",
            imageRes = R.drawable.reportes_certificados,
            onClick = onGoReportes
        )

        Spacer(Modifier.height(14.dp))

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
            .height(170.dp)
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // La lista inicia VACÍA
    val reports = remember {
        mutableStateListOf<CertReport>()
    }

    var showPaymentDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BgSoft,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgSoft)
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Reportes Certificados",
                            color = TextIcon,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.certificado),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(160.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // --- BLOQUE: Comprar reporte ---
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(containerColor = CardWhite),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "¿Quieres comprar un reporte?",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextIcon
                            )
                            Text(
                                "Paga por el acceso al reporte y este aparecerá en tu lista de reportes comprados.",
                                color = TextIcon.copy(alpha = 0.85f)
                            )
                            Spacer(Modifier.height(8.dp))

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                FilledTonalButton(onClick = { showPaymentDialog = true }) {
                                    Text("Pagar")
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }

                // --- LISTA DE REPORTES COMPRADOS (inicia vacía) ---
                items(reports) { report ->
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(containerColor = CardWhite),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                "Fecha: ${report.fecha}",
                                fontWeight = FontWeight.SemiBold,
                                color = TextIcon
                            )
                            Text(
                                "Periodo: ${report.periodo}",
                                color = TextIcon.copy(alpha = 0.9f)
                            )
                            Text(
                                "Total valorizado: ${report.toneladas}",
                                color = TextIcon.copy(alpha = 0.9f)
                            )

                            Spacer(Modifier.height(8.dp))

                            FilledTonalButton(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Descarga simulada: ${report.archivo}"
                                        )
                                    }
                                }
                            ) {
                                Text("Descargar")
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(48.dp)) }
            }

            // Diálogo de pago
            if (showPaymentDialog) {
                PaymentDialog(
                    onDismiss = { showPaymentDialog = false },
                    onSuccess = { purchasedReport ->
                        reports.add(0, purchasedReport)
                        scope.launch {
                            snackbarHostState.showSnackbar("Pago exitoso. Reporte agregado.")
                        }
                        showPaymentDialog = false
                    }
                )
            }

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(snackbarHostState)
            }
        }
    }
}

@Composable
private fun PaymentDialog(
    onDismiss: () -> Unit,
    onSuccess: (CertReport) -> Unit
) {
    val scope = rememberCoroutineScope()
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var cardName by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }
    var cvc by rememberSaveable { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Dialog centrado con tarjeta bonita
    androidx.compose.ui.window.Dialog(
        onDismissRequest = { if (!loading) onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x33000000)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)           // <-- un poco más grande
                    .padding(horizontal = 8.dp),
                color = CardWhite,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                tonalElevation = 6.dp,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .widthIn(max = 520.dp)       // <-- ancho máximo mayor
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Encabezado
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Generar pago",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextIcon
                        )
                        TextButton(onClick = { if (!loading) onDismiss() }) {
                            Text("Cerrar")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(PillGreen.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = TextIcon,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Text(
                        "Ingresa los datos de tu tarjeta para generar tu reporte certificado.",
                        textAlign = TextAlign.Center,
                        color = TextIcon.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Campos de tarjeta
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }.take(19)
                            cardNumber = digits.chunked(4).joinToString(" ")
                        },
                        label = { Text("Número de tarjeta") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = cardName,
                        onValueChange = { cardName = it },
                        label = { Text("Nombre en la tarjeta") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = expiry,
                            onValueChange = { expiry = it.take(5) },
                            // ⬇️ solo "MM/AA" para que no se corte
                            label = { Text("MM/AA") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = cvc,
                            onValueChange = { cvc = it.filter { ch -> ch.isDigit() }.take(4) },
                            label = { Text("CVC") },
                            singleLine = true,
                            modifier = Modifier.width(120.dp)
                        )
                    }

                    if (loading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = {
                            val cleanCard = cardNumber.replace(" ", "")
                            if (cleanCard.length < 13 ||
                                cardName.isBlank() ||
                                expiry.length < 4 ||
                                cvc.length < 3
                            ) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Revisa los datos de la tarjeta."
                                    )
                                }
                                return@Button
                            }

                            loading = true
                            scope.launch {
                                // Simulación de procesamiento
                                kotlinx.coroutines.delay(1000)
                                loading = false

                                // Crear reporte comprado usando Calendar / SimpleDateFormat (API 24+)
                                val cal = java.util.Calendar.getInstance()
                                val sdfFecha = java.text.SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    java.util.Locale.getDefault()
                                )
                                val sdfArchivo = java.text.SimpleDateFormat(
                                    "yyyyMMdd_HHmmss",
                                    java.util.Locale.getDefault()
                                )

                                val fecha = sdfFecha.format(cal.time)
                                val nombreMes = cal.getDisplayName(
                                    java.util.Calendar.MONTH,
                                    java.util.Calendar.LONG,
                                    java.util.Locale.getDefault()
                                ) ?: "Mes"
                                val year = cal.get(java.util.Calendar.YEAR)

                                val periodo =
                                    "Reporte comprado: ${
                                        nombreMes.replaceFirstChar { it.uppercase() }
                                    } $year"

                                val archivoName =
                                    "Certificado_COMPRADO_${
                                        sdfArchivo.format(cal.time)
                                    }.pdf"

                                val nuevo = CertReport(
                                    fecha = fecha,
                                    periodo = periodo,
                                    toneladas = "—",
                                    archivo = archivoName
                                )

                                onSuccess(nuevo)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PillGreen,
                            contentColor = TextIcon
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                        enabled = !loading
                    ) {
                        Text("Pagar")
                    }

                    Spacer(Modifier.height(8.dp))

                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        SnackbarHost(snackbarHostState)
                    }
                }
            }
        }
    }
}



/* =====================  CUMPLIMIENTO REP  ===================== */

private class MaterialMetric(
    val nombre: String,
    porcentajeInicial: Float
) {
    var porcentaje by mutableStateOf(porcentajeInicial)
}

@Composable
private fun RepCumplimientoScreen() {
    var metaAnual by rememberSaveable { mutableFloatStateOf(0.30f) }
    var avanceActual by rememberSaveable { mutableFloatStateOf(0.18f) }

    val materiales = remember {
        mutableStateListOf(
            MaterialMetric("PET", 0.18f),
            MaterialMetric("HDPE", 0.10f),
            MaterialMetric("Cartón / Papel", 0.05f),
            MaterialMetric("Vidrio", 0.01f),
            MaterialMetric("Metales", 0.03f)
        )
    }

    var resultadoCumple by remember { mutableStateOf<Boolean?>(null) }
    var mensajePorQueBajo by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun generarExplicacion(total: Float): String {
        val causas = mutableListOf<String>()
        if (total < 0.70f) {
            if (total < 0.50f) causas.add("Baja recolección selectiva y separación en origen.")
            if (materiales.any { it.porcentaje < 0.05f }) causas.add("Algunos materiales (p. ej. vidrio o cartón) están con porcentajes muy bajos.")
            if (avanceActual < metaAnual * 0.5f) causas.add("Avance general por debajo de la mitad de la meta anual.")
            causas.add("Necesidad de mayor participación ciudadana y/o infraestructura de recolección.")
        }
        return if (causas.isEmpty()) "Revisar datos de entrada y la metodología de cálculo." else causas.joinToString(" ")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgSoft)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Cumplimiento REP",
            color = TextIcon,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = CardWhite)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Meta anual: ${(metaAnual * 100).toInt()}%", color = TextIcon)
                Slider(value = metaAnual, onValueChange = { metaAnual = it.coerceIn(0f, 1f) }, valueRange = 0f..1f, steps = 98)

                Text("Avance actual: ${(avanceActual * 100).toInt()}%", color = TextIcon)
                Slider(value = avanceActual, onValueChange = { avanceActual = it.coerceIn(0f, 1f) }, valueRange = 0f..1f, steps = 98)

                val progreso = (avanceActual / metaAnual.coerceAtLeast(0.01f)).coerceAtMost(1f)
                LinearProgressIndicator(progress = progreso, modifier = Modifier.fillMaxWidth())
                Text("Progreso vs meta: ${(progreso * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = TextIcon)
            }
        }

        Text("Materiales", style = MaterialTheme.typography.titleMedium, color = TextIcon)

        materiales.forEach { m ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = CardWhite)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(m.nombre, fontWeight = FontWeight.SemiBold, color = TextIcon)
                        Text("${(m.porcentaje * 100).toInt()}%", color = TextIcon)
                    }
                    Spacer(Modifier.height(6.dp))
                    Slider(value = m.porcentaje, onValueChange = { m.porcentaje = it.coerceIn(0f, 1f) }, valueRange = 0f..1f, steps = 98)
                    LinearProgressIndicator(progress = m.porcentaje, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            FilledTonalButton(onClick = {
                val total = materiales.fold(0f) { acc, mm -> acc + mm.porcentaje }
                val totalClamped = total.coerceIn(0f, 1f)
                if (totalClamped >= 0.70f) {
                    resultadoCumple = true
                    mensajePorQueBajo = ""
                } else {
                    resultadoCumple = false
                    mensajePorQueBajo = generarExplicacion(totalClamped)
                }
            }) {
                Icon(Icons.Outlined.FileDownload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Calcular")
            }
        }

        /* resultado y snackbar (igual que tu lógica) */
        resultadoCumple?.let { cumple ->
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = CardWhite)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (cumple) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = "Cumple", tint = Color(0xFF2E7D32), modifier = Modifier.size(36.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("¡Meta alcanzada!", style = MaterialTheme.typography.titleMedium, color = TextIcon)
                                Text("El total de porcentajes es mayor o igual a 70%.", color = TextIcon)
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            FilledTonalButton(onClick = { scope.launch { snackbarHostState.showSnackbar("Descarga simulada: Certificado_Cumplimiento_OK.pdf") } }) {
                                Icon(Icons.Outlined.FileDownload, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Descargar certificado")
                            }
                        }
                    } else {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Warning, contentDescription = "No cumple", tint = Color(0xFFB00020), modifier = Modifier.size(36.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Por debajo del 70%", style = MaterialTheme.typography.titleMedium, color = TextIcon)
                                Text("Porcentaje total: ${(materiales.sumOf { it.porcentaje.toDouble() } * 100).toInt()}%", color = TextIcon)
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("Posibles causas:", fontWeight = FontWeight.SemiBold, color = TextIcon)
                        Text(mensajePorQueBajo, color = TextIcon)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            FilledTonalButton(onClick = { scope.launch { snackbarHostState.showSnackbar("Descarga simulada: Informe_Observaciones_Cumplimiento.pdf") } }) {
                                Icon(Icons.Outlined.FileDownload, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Descargar informe")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { SnackbarHost(snackbarHostState) }
        Spacer(Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRepCumplimiento() {
    RepCumplimientoScreen()
}
/* =====================  PERFIL  ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreene(
    emailFromAuth: String? = null,
    onLogout: (() -> Unit)? = null
) {
    val ctx = LocalContext.current

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember(photoUri) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(photoUri) { bitmap = photoUri?.let { loadBitmapFromUri(ctx, it) } }

    var nombre by rememberSaveable { mutableStateOf("Nombre Empresa") }
    var correo by rememberSaveable { mutableStateOf(emailFromAuth.orEmpty().ifEmpty { "rep@empresa.com" }) }
    var contrasena by rememberSaveable { mutableStateOf("") }

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (!success) photoUri = null }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = createTempImageFile(ctx)
            val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
            photoUri = uri
            takePicture.launch(uri)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1B4332)
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(0xFF5C8C5F), CircleShape)
                        .background(Color(0xFFB8F8AD).copy(alpha = 0.25f), CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(0xFF1B4332), CircleShape)
                        .background(Color(0xFFB8F8AD).copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin foto", fontWeight = FontWeight.SemiBold, color = Color(0xFF1B4332))
                }
            }

            Spacer(Modifier.height(20.dp))

            TextButton(
                onClick = {
                    val granted = ContextCompat.checkSelfPermission(
                        ctx, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (granted) {
                        val file = createTempImageFile(ctx)
                        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
                        photoUri = uri
                        takePicture.launch(uri)
                    } else {
                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cambiar foto")
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { /* TODO: guardar cambios */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PillGreen, contentColor = TextIcon)
            ) {
                Text("Guardar cambios")
            }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onLogout?.invoke() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}


/* ===== helpers ===== */

private fun createTempImageFile(ctx: android.content.Context): File {
    val dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("profile_", ".jpg", dir)
}

private fun loadBitmapFromUri(ctx: android.content.Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(ctx.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(ctx.contentResolver, uri)
        }
    } catch (_: Exception) { null }
}
