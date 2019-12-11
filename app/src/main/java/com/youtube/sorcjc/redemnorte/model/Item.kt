package com.youtube.sorcjc.redemnorte.model

import com.google.gson.annotations.SerializedName

data class Item(
    // Codes
    @SerializedName("QR_code")
    var qr: String,

    @SerializedName("patrimonial_code")
    var patrimonial: String, // General

    @SerializedName("denominacion")
    var description: String,

    // Parent (header)
    @SerializedName("hoja_id")
    var hoja_id: String? = null,

    // Meta-data
    @SerializedName("old_code")
    var old_code: String? = null,

    @SerializedName("old_year")
    var old_year: String? = null,

    @SerializedName("marca")
    var brand: String? = null,

    @SerializedName("modelo")
    var model: String? = null,

    @SerializedName("serie")
    var series: String? = null,

    @SerializedName("color")
    var color: String? = null,

    // Dimensions
    @SerializedName("largo")
    var dimLong: String? = null,

    @SerializedName("ancho")
    var dimWidth: String? = null,

    @SerializedName("alto")
    var dimHigh: String? = null,

    // Status
    @SerializedName("condicion")
    var preservation: String? = null,

    @SerializedName("etiquetado")
    var etiquetado: String? = null,

    @SerializedName("operativo")
    var isOperative: String? = null,

    @SerializedName("observacion")
    var observation: String? = null,

    // Photo
    @SerializedName("photo_extension")
    var photo_extension: String? = null,

    // Used when the data is taken using an old code
    var codigoActivo: String? = null
)