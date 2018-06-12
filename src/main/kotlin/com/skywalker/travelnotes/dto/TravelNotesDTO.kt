package com.skywalker.travelnotes.dto

import java.util.*

data class TravelNotesDTO(
        var travelNotesId: Long=0L,
        var title: String? = null,
        var userName: String? = null,
        var nickname: String? = null,
        var headImage: String? = null,
        var addressName: String? = null,
        var addressCoordinate: String? = null,
        var content: String? = null,
        var timeCreate: Date? = null
)
{
        var notesLikeCount: Long = 0
        var notesMsgCount: Long = 0
        var listTravelNotesImgDTO: List<TravelNotesImgDTO>? = null
}
