package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerTravelNotes(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var travelNotesId: Long,
        var postUserId: Long,
        var title: String,
        var content: String,
        var addressName: String,
        var addressCoordinate: String,
        @Temporal(TemporalType.TIMESTAMP)
        var timeCreate: Date,
        var isDelete: String
)
