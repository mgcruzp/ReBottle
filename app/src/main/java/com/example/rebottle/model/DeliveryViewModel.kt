package com.example.rebottle.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class Delivery(
    val id: String = "",
    val collectorId: String = "",
    val place: String = "",
    val weightKg: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)

class DeliveryRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
) {
    private val deliveriesRef get() = rootRef.child("deliveries")

    suspend fun createDelivery(place: String, weightKg: Double): Result<String> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No autenticado")
        val key = deliveriesRef.push().key ?: error("No se pudo generar ID")
        val d = Delivery(
            id = key,
            collectorId = uid,
            place = place,
            weightKg = weightKg,
            createdAt = System.currentTimeMillis()
        )
        deliveriesRef.child(key).setValue(d).await()
        key
    }

    data class CollectorStats(
        val count: Int = 0,
        val totalKg: Double = 0.0,
        val totalEarnings: Long = 0L  // COP
    )

    // Cambia la tarifa si quieres
    private val RATE_COP_PER_KG = 500L

    /** Stats en vivo para el recolector autenticado */
    fun listenMyStats(): Flow<CollectorStats> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(CollectorStats()) // vacío
            close()                   // cerrar sin error
            return@callbackFlow
        }

        val q = deliveriesRef.orderByChild("collectorId").equalTo(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) {
                var c = 0
                var kg = 0.0
                s.children.forEach { snap ->
                    val d = snap.getValue(Delivery::class.java)
                    if (d != null) { c++; kg += d.weightKg }
                }
                val earnings = (kg * RATE_COP_PER_KG).toLong()
                trySend(CollectorStats(c, kg, earnings))
            }
            override fun onCancelled(e: DatabaseError) {
                // <- Al perder permisos (logout), no lances excepción
                trySend(CollectorStats()) // emite vacío
                close()                   // cierra suave
            }
        }

        // Además: cierra automáticamente si el usuario se desloguea
        val authListener = FirebaseAuth.AuthStateListener { fa ->
            if (fa.currentUser == null) {
                trySend(CollectorStats())
                close()
            }
        }
        auth.addAuthStateListener(authListener)

        q.addValueEventListener(listener)
        awaitClose {
            q.removeEventListener(listener)
            auth.removeAuthStateListener(authListener)
        }
    }

}

data class RegisterUiState(
    val loading: Boolean = false,
    val successId: String? = null,
    val error: String? = null
)

class RegisterDeliveryViewModel(
    private val repo: DeliveryRepository = DeliveryRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun register(place: String, weightText: String) {
        val w = weightText.replace(",", ".").toDoubleOrNull()
        if (place.isBlank() || w == null || w <= 0) {
            _state.value = RegisterUiState(error = "Ingresa lugar y un peso válido (> 0)")
            return
        }
        viewModelScope.launch {
            _state.value = RegisterUiState(loading = true)
            val res = repo.createDelivery(place, w)
            _state.value = res.fold(
                onSuccess = { RegisterUiState(successId = it) },
                onFailure = { RegisterUiState(error = it.message ?: "Error registrando entrega") }
            )
        }
    }

    fun clear() { _state.value = RegisterUiState() }
}


data class HomeStatsUiState(
    val loading: Boolean = true,
    val deliveries: Int = 0,
    val totalKg: String = "0.0",
    val totalCop: String = "$0",
    val error: String? = null
)

class CollectorHomeViewModel(
    private val repo: DeliveryRepository = DeliveryRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(HomeStatsUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.listenMyStats().collect { s ->
                _state.value = HomeStatsUiState(
                    loading = false,
                    deliveries = s.count,
                    totalKg = String.format("%.1f kg", s.totalKg),
                    totalCop = formatCop(s.totalEarnings),
                    error = null
                )
            }
        }
    }

    private fun formatCop(value: Long): String {
        val str = "%,d".format(value).replace(',', '.')
        return "$$str"
    }
}