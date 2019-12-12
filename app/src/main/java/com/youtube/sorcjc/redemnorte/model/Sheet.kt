package com.youtube.sorcjc.redemnorte.model

data class Sheet (
        var id: String? = null,

        // Local
        var place: String? = null,

        // Ubicación
        var location: String? = null,

        // Oficina
        var office: String? = null,

        // Usuario responsable
        var responsible_user: String? = null,

        // Cargo
        var position: String? = null,

        var area: String? = null,
        var ambient: String? = null,

        // Author
        var user_id: String? = null,

        var created_at: String? = null,
        var pending: Boolean,
        var observation: String? = null,
        var printed: Boolean
)