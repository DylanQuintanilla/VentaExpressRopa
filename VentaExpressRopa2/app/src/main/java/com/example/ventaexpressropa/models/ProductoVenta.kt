package com.example.ventaexpressropa.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductoVenta(
    var productoId: String? = null,
    var nombre: String? = null,
    var cantidad: Int? = null,
    var precioUnitario: Double? = null
) : Parcelable {
    constructor() : this(null, null, null, null)
}