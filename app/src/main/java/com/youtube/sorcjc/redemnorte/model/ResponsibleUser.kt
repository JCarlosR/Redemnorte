package com.youtube.sorcjc.redemnorte.model

data class ResponsibleUser (var id: Int, var name: String) {
    override fun toString(): String {
        return name
    }
}