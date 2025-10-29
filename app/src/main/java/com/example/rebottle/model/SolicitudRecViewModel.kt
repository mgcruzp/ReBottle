package com.example.rebottle.model


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class PickupRequest(
    val id: String = "",            // key de RTDB
    val userId: String = "",        // UID de Firebase Auth
    val userName: String = "",      // opcional: para mostrar rápido sin más lecturas
    val brand: String = "",         // “marca” que ingresas en la UI
    val date: String = "",          // “fecha” (ej. 2025-10-28)
    val time: String = "",          // “hora” (ej. 14:30)
    val address: String = "",       // “ubicación” (texto libre)
    val status: String = "PENDIENTE", // PENDIENTE | ASIGNADA | COMPLETADA | CANCELADA
    val createdAt: Long = System.currentTimeMillis()
)




class PickupRequestRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
) {
    private val requestsRef get() = rootRef.child("requests")
    private val usersRef get() = rootRef.child("users")

    suspend fun getRole(): String {
        val uid = auth.currentUser?.uid ?: return ""
        return usersRef.child(uid).child("role").get().await().getValue(String::class.java) ?: ""
    }


    suspend fun createRequest(
        brand: String,
        date: String,
        time: String,
        address: String
    ): Result<String> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No autenticado")

        // opcional: traer nombre del usuario para denormalizar
        val userName = usersRef.child(uid).child("name")
            .get().await().getValue(String::class.java) ?: ""

        val key = requestsRef.push().key ?: error("No se pudo generar ID")

        val req = PickupRequest(
            id = key,
            userId = uid,
            userName = userName,
            brand = brand,
            date = date,
            time = time,
            address = address,
            status = "PENDIENTE"
        )

        requestsRef.child(key).setValue(req).await()
        key
    }

    fun listenPending(): Flow<List<PickupRequest>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            close(IllegalStateException("No autenticado"))
            return@callbackFlow
        }

        // 1) Leer rol de forma suspendida (SIN callbacks)
        val role = try {
            usersRef.child(uid).child("role").get().await().getValue(String::class.java) ?: ""
        } catch (e: Exception) {
            close(e); return@callbackFlow
        }

        // 2) Construir la query según rol
        val query: Query = if (role == "RECOLECTOR" || role == "EMPRESA_REP") {
            requestsRef.orderByChild("status").equalTo("PENDIENTE")
        } else {
            requestsRef.orderByChild("userId").equalTo(uid)
        }

        // 3) Listener y emisión
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val all = snapshot.children.mapNotNull { it.getValue(PickupRequest::class.java) }
                val result = if (role == "RECOLECTOR" || role == "EMPRESA_REP") {
                    all
                } else {
                    all.filter { it.status == "PENDIENTE" }
                }.sortedByDescending { it.createdAt }
                trySend(result)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }

        query.addValueEventListener(listener)

        // 4) CIERRE: aquí sí va awaitClose, al nivel del callbackFlow
        awaitClose { query.removeEventListener(listener) }
    }

    /** (Opcional) Solo para recolectores/empresa: todas las PENDIENTE directamente */
    fun listenAllPending(): Flow<List<PickupRequest>> = callbackFlow {
        val query = requestsRef.orderByChild("status").equalTo("PENDIENTE")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.getValue(PickupRequest::class.java) }
                    .sortedByDescending { it.createdAt }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    /** (Opcional) actualizar status (asignar/completar/cancelar) */
    suspend fun updateStatus(requestId: String, status: String): Result<Unit> = runCatching {
        requestsRef.child(requestId).child("status").setValue(status).await()
    }

}


data class CreateRequestUiState(
    val loading: Boolean = false,
    val successId: String? = null,
    val error: String? = null
)

class PickupRequestViewModel(
    private val repo: PickupRequestRepository = PickupRequestRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(CreateRequestUiState())
    val state = _state.asStateFlow()

    fun create(brand: String, date: String, time: String, address: String) {
        if (brand.isBlank() || date.isBlank() || time.isBlank() || address.isBlank()) {
            _state.value = CreateRequestUiState(error = "Completa todos los campos")
            return
        }
        viewModelScope.launch {
            _state.value = CreateRequestUiState(loading = true)
            val result = repo.createRequest(brand, date, time, address)
            _state.value = result.fold(
                onSuccess = { CreateRequestUiState(loading = false, successId = it) },
                onFailure = { CreateRequestUiState(loading = false, error = it.message ?: "Error desconocido") }
            )
        }
    }

    fun clearMessages() { _state.value = _state.value.copy(successId = null, error = null) }
}

data class PendingListUiState(
    val loading: Boolean = true,
    val items: List<PickupRequest> = emptyList(),
    val error: String? = null
)

class PendingRequestsViewModel(
    private val repo: PickupRequestRepository = PickupRequestRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(PendingListUiState())
    val state: StateFlow<PendingListUiState> = _state

    init {
        viewModelScope.launch {
            repo.listenPending()
                .catch { _state.value = PendingListUiState(loading = false, error = it.message) }
                .collect { list ->
                    _state.value = PendingListUiState(loading = false, items = list)
                }
        }
    }

    fun refresh() {
        // Con ValueEventListener ya es "en vivo"; podrías reenlazar si cambias a consultas por paginación.
    }
}