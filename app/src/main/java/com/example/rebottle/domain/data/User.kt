package com.example.rebottle.domain.data

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: Role = Role.USUARIO,
    val createdAt: Long = System.currentTimeMillis()
)
