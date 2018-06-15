package com.skywalker.travelnotes.dto

import java.util.*

data class TravelNotesParamDTO(
    var travelNotesId: Long? = null,
    var postUserId: Long? = null,
    var date: Date? = null,
    var dateAfter: Date? = null
)
