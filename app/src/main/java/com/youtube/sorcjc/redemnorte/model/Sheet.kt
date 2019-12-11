package com.youtube.sorcjc.redemnorte.model

data class Sheet (
    var id: String? = null,

    var place: String? = null, // Local
    var location: String? = null, // Ubicación
    var office: String? = null, // Oficina
    var responsible_user: String? = null, // Usuario responsable
    var position: String? = null, // Cargo
    var area: String? = null,
    var ambient: String? = null,
    var user_id: String? = null, // Inventariador

    var created_at: String? = null,
    var activo: String? = null,
    var observation: String? = null,
    var impreso: String? = null
)