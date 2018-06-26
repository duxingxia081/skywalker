package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerTravelNotes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var travelNotesId: Long = 0L,
    var postUserId: Long = 0L,
    var title: String = "",
    var content: String? = null,
    var addressName: String? = null,
    var addressCoordinate: String? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var timeCreate: Date? = null
)
