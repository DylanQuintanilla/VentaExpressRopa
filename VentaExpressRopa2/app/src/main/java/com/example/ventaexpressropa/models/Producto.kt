package com.example.ventaexpressropa.models

data class Producto(
    var id: String? = null,
    var nombre: String? = null,
    var descripcion: String? = null,
    var precio: Double? = null,
    var stock: Int? = null
) {
    constructor() : this(null, null, null, null, null)
}