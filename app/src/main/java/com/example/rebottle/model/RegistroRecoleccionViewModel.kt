package com.example.rebottle.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class RegistroRecoleccion(
    val id: String = "",
    val recolectorId: String = "",
    val recolectorNombre: String = "",
    val lugar: String = "",
    val peso: Double = 0.0,
    val fotoUri: String = "",
    val fecha: Long = 0L
)

class RegistroRecoleccionViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun guardarRegistro(
        lugar: String,
        peso: Double,
        fotoUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = auth.currentUser
                    ?: throw Exception("Usuario no autenticado")

                val recolectorId = currentUser.uid

                // Subir foto
                var fotoUrl = ""
                if (fotoUri != null) {
                    val fotoRef = storage.reference
                        .child("registros_fotos/${recolectorId}_${System.currentTimeMillis()}.jpg")

                    fotoRef.putFile(fotoUri).await()
                    fotoUrl = fotoRef.downloadUrl.await().toString()
                }

                // Crear registro
                val registroId = UUID.randomUUID().toString()
                val registro = RegistroRecoleccion(
                    id = registroId,
                    recolectorId = recolectorId,
                    recolectorNombre = currentUser.email ?: "",
                    lugar = lugar,
                    peso = peso,
                    fotoUri = fotoUrl,
                    fecha = System.currentTimeMillis()
                )

                // Guardar en Firestore
                firestore.collection("registros_recoleccion")
                    .document(registroId)
                    .set(registro)
                    .await()

                Log.d("FIRESTORE", "Registro guardado exitosamente: $registroId")
                _isLoading.value = false
                onSuccess()

            } catch (e: Exception) {
                Log.e("FIRESTORE", "Error al guardar registro", e)
                _isLoading.value = false
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    // ✅ Mover esta función AQUÍ
    fun obtenerTotales(
        onResult: (Int, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: throw Exception("Usuario no autenticado")

                val snapshot = firestore.collection("registros_recoleccion")
                    .whereEqualTo("recolectorId", currentUser.uid)
                    .get()
                    .await()

                val totalEntregas = snapshot.size()
                val totalPeso = snapshot.documents.sumOf { it.getDouble("peso") ?: 0.0 }

                onResult(totalEntregas, totalPeso)
            } catch (e: Exception) {
                onError(e.message ?: "Error al obtener totales")
            }
        }
    }
}
