package com.youtube.sorcjc.redemnorte.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.youtube.sorcjc.redemnorte.R

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

        var status: Int,
        var labeled: Boolean,
        var operative: Boolean,

        // Meta-data
        @SerializedName("old_code")
        var old_code: String? = null,
        @SerializedName("year")
        var old_year: String? = null,
        @SerializedName("responsible_name")
        var responsible: String? = null,

        var observation: String? = null,

        // Photo
        var image: String? = null

        // Used when the data is taken using an old code
        // var codigoActivo: String? = null
) {
    fun photoUrl(): String {
        if (image.isNullOrEmpty())
            return "https://redemnorte.com/images/users/logo.png"

        return "https://redemnorte.com/images/items/$image"
    }

    fun getStatusText(context: Context): String {
        val statusArr = context.resources.getStringArray(R.array.status_options)
        return statusArr[status]
    }
}