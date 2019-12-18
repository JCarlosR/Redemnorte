package com.youtube.sorcjc.redemnorte.io.response

import com.youtube.sorcjc.redemnorte.model.Item

data class ExpectedDataResponse (
    val found: Boolean,
    val alreadyRegistered: Boolean,
    val item: Item
)