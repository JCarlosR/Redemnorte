package com.youtube.sorcjc.redemnorte.model

import com.google.gson.annotations.SerializedName

data class BienConsolidado(
    // General
    @SerializedName("denominacion")
    var description: String,

    @SerializedName("marca")
    var brand: String? = null,

    @SerializedName("modelo")
    var model: String? = null,

    @SerializedName("serie")
    var series: String? = null,

    // Status

    @SerializedName("estado")
    var estado: String? = null,

    @SerializedName("empleado")
    var empleado: String? = null,

    @SerializedName("situacion")
    var situacion: String? = null,

    var ubicacion: String? = null,
    var local: String? = null
)