package com.example.rebottle.domain.data

enum class Role {
    USUARIO, RECOLECTOR, EMPRESA_REP;

    val displayName: String
        get() = name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}
