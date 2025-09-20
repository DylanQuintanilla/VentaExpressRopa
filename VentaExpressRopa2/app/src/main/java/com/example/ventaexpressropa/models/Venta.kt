package com.example.ventaexpressropa.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Venta(
    var id: String? = null,
    var clienteId: String? = null,
    var clienteNombre: String? = null,
    var productos: List<ProductoVenta>? = null,
    var total: Double? = null,
    var fecha: Long? = null
) : Parcelable {
    constructor() : this(null, null, null, null, null, null)
}