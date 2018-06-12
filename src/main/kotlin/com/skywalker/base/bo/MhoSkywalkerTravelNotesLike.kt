package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerTravelNotesLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var likeId: Long = 0L,
    var travelNotesId: Long = 0L,
    var dolikeUserId: Long? = null,
    var timeCreate: Date = Date()
)