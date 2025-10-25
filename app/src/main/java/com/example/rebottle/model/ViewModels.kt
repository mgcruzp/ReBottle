package com.example.rebottle.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebottle.domain.data.Role
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Modelo de usuario
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: Role = Role.USUARIO,
    val createdAt: Long = System.currentTimeMillis()
)

// estado de autenticación
data class UserAuthState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val role: Role? = null,
    val emailError: String = "",
    val passError: String = "",
    val isLoading: Boolean = false,
    val currentUser: User? = null
)

class UserAuthViewModel : ViewModel() {

    private val _user = MutableStateFlow(UserAuthState())
    val user = _user.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // Registro de usuario
    fun registerUser(
        name: String,
        email: String,
        password: String,
        role: Role,
        onSuccess: (Role) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _user.value = _user.value.copy(isLoading = true)
            try {
                //Crear usuario en Firebase Auth
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("No se pudo crear el usuario")

                //Crear objeto de usuario
                val userData = User(
                    uid = uid,
                    name = name,
                    email = email,
                    role = role,
                    createdAt = System.currentTimeMillis()
                )

                // Se Guarda el usuario en Realtime Database
                database.child("users").child(uid).setValue(userData).await()

                // Actualiza estado local
                _user.value = _user.value.copy(
                    isLoading = false,
                    currentUser = userData
                )

                //Notifica éxito
                onSuccess(role)

            } catch (e: Exception) {
                _user.value = _user.value.copy(isLoading = false)
                onError(e.message ?: "Error al registrar usuario")
            }
        }
    }

    // Login de usuarioo
    fun loginUser(
        email: String,
        password: String,
        onSuccess: (Role) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _user.value = _user.value.copy(isLoading = true)
            try {
                //Autenticar usuario con Firebase Auth
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("Error al iniciar sesión")

                //Obtener datos del usuario desde Realtime Database
                val snapshot = database.child("users").child(uid).get().await()
                val userData = snapshot.getValue(User::class.java)
                    ?: throw Exception("Usuario no encontrado en la base de datos")

                //Actualizar estado local
                _user.value = _user.value.copy(
                    isLoading = false,
                    currentUser = userData
                )

                //Notificar éxito con el rol
                onSuccess(userData.role)

            } catch (e: Exception) {
                _user.value = _user.value.copy(isLoading = false)
                onError(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    // Obtiene rol del usuario actual
    fun getCurrentUserRole(onResult: (Role?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(null)
            return
        }

        database.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(User::class.java)
                    _user.value = _user.value.copy(currentUser = userData)
                    onResult(userData?.role)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null)
                }
            })
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.signOut()
                _user.value = UserAuthState()
                onComplete()
            } catch (e: Exception) {
                auth.signOut()
                _user.value = UserAuthState()
                onComplete()
            }
        }
    }

    fun updateEmail(newEmail: String) {
        _user.value = _user.value.copy(email = newEmail)
    }

    fun updatePassword(newPass: String) {
        _user.value = _user.value.copy(password = newPass)
    }

    fun updateEmailError(error: String) {
        _user.value = _user.value.copy(emailError = error)
    }

    fun updatePassError(error: String) {
        _user.value = _user.value.copy(passError = error)
    }
}