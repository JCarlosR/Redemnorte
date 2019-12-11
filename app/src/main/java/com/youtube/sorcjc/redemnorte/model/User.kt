package com.youtube.sorcjc.redemnorte.model

data class User(
    var id: Int,
    var name: String,
    var username: String,
    var email: String? = null,
    var role: Int
)