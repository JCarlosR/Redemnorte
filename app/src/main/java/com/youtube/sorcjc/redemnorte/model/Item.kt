package com.youtube.sorcjc.redemnorte.model

import com.google.gson.annotations.SerializedName

data class Item(
        var id: Int = -1,
        var inventory_code: String, // QR code

        @SerializedName("patrimonial_code")
        var patrimonial: String, // General

        var denomination: String,

        // Parent (header)
        @SerializedName("sheet_id")
        var sheetId: Int = -1,

        var brand: String? = null,
        var model: String? = null,
        var series: String? = null,
        var color: String? = null,

        // Dimensions
        var length: String? = null,
        var width: String? = null,
        var height: String? = null,

        var status: String = "",
        var labeled: Boolean,
        var operative: Boolean,

        // Meta-data
        @SerializedName("old_code")
        var old_code: String? = null,
        @SerializedName("year")
        var old_year: String? = null,

        var observation: String? = null,

        // Photo
        var image: String? = null

        // Used when the data is taken using an old code
        // var codigoActivo: String? = null
) {
    fun photoUrl(): String {
        return "https://redemnorte.com/images/items/$image"
    }
}