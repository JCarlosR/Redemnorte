package com.youtube.sorcjc.redemnorte.model

data class Area (val id: Int, val name: String) {
    override fun toString(): String {
        return name
    }
}