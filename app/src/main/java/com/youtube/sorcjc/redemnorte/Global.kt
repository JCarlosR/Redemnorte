package com.youtube.sorcjc.redemnorte

object Global {
    fun getProductPhotoUrl(sheetId: String, qrCode: String, extension: String): String {
        return "https://redemnorte.com/images/items/$sheetId-$qrCode.$extension"
    }
}