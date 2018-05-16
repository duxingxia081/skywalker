package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerTravelNotes(
        @Id
        @GeneratedValue
        var travelNotesId: Long,
        var postUserId: Long,
        var title: String,
        var content: String,
        var addressName: String,
        var addressCoordinate: String,
        var timeCreate: Date,
        var isDelete: String
)
