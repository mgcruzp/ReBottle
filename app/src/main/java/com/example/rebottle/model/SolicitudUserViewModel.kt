package com.example.rebottle.model

import com.google.firebase.database.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


class SolicitudRequestRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
) {
    private val requestsRef get() = rootRef.child("requests")
    private val usersRef get() = rootRef.child("users")

    // ... tu createRequest(...) aquí ...

    /** Mis solicitudes en estado PENDIENTE (del usuario autenticado) */
    fun listenMyPending(): Flow<List<PickupRequest>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            close(IllegalStateException("No auth"))
            return@callbackFlow
        }

        val q: Query = requestsRef.orderByChild("userId").equalTo(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                val items = snap.children
                    .mapNotNull { it.getValue(PickupRequest::class.java) }
                    .filter { it.status == "PENDIENTE" }
                    .sortedByDescending { it.createdAt }
                trySend(items)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        q.addValueEventListener(listener)
        awaitClose { q.removeEventListener(listener) }
    }

    suspend fun updateStatus(requestId: String, status: String): Result<Unit> = runCatching {
        requestsRef.child(requestId).child("status").setValue(status).await()
    }

    fun listenMyByStatuses(statuses: Set<String>): Flow<List<PickupRequest>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList()); close(IllegalStateException("No auth")); return@callbackFlow
        }

        val q: Query = requestsRef.orderByChild("userId").equalTo(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                val items = snap.children
                    .mapNotNull { it.getValue(PickupRequest::class.java) }
                    .filter { it.status in statuses }
                    .sortedByDescending { it.createdAt }
                trySend(items)
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }

        q.addValueEventListener(listener)
        awaitClose { q.removeEventListener(listener) }
    }

}




data class MyPendingUiState(
    val loading: Boolean = true,
    val items: List<PickupRequest> = emptyList(),
    val error: String? = null,
    val actionLoadingId: String? = null,
    val actionMessage: String? = null
)

class MyPendingRequestsViewModel(
    private val repo: SolicitudRequestRepository = SolicitudRequestRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(MyPendingUiState())
    val state: StateFlow<MyPendingUiState> = _state

    init {
        viewModelScope.launch {
            repo.listenMyPending()
                .catch { _state.value = MyPendingUiState(loading = false, error = it.message) }
                .collect { list -> _state.value = MyPendingUiState(loading = false, items = list) }
        }
    }
    private fun setStatus(id: String, status: String) = viewModelScope.launch {
        _state.value = _state.value.copy(actionLoadingId = id, actionMessage = null)
        val res = repo.updateStatus(id, status)
        _state.value = if (res.isSuccess) {
            _state.value.copy(actionLoadingId = null, actionMessage = "Estado: $status")
        } else {
            _state.value.copy(actionLoadingId = null, actionMessage = res.exceptionOrNull()?.message ?: "Error")
        }
    }

    fun markCompleted(id: String) = setStatus(id, "COMPLETADA")
    fun cancel(id: String)       = setStatus(id, "CANCELADA")

    fun clearMessage() {
        _state.value = _state.value.copy(actionMessage = null)
    }
}