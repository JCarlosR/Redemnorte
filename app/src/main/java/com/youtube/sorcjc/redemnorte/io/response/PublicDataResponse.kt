package com.youtube.sorcjc.redemnorte.io.response

import com.youtube.sorcjc.redemnorte.model.Area
import com.youtube.sorcjc.redemnorte.model.Place
import com.youtube.sorcjc.redemnorte.model.ResponsibleUser
import java.util.*

data class PublicDataResponse (
    val responsibleUsers: ArrayList<ResponsibleUser>,
    val areas: ArrayList<Area>,
    val places: ArrayList<Place>
)