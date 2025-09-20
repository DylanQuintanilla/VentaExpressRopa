package com.example.ventaexpressropa.models

data class Cliente(
    var id: String? = null,
    var nombre: String? = null,
    var correo: String? = null,
    var telefono: String? = null
) {
    constructor() : this(null, null, null, null)
}