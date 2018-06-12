package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler", "fieldHandler"])
data class MhoSkywalkerTravelNotesImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var imageId: Long = 0L,
    var travelNotesId: Long = 0L,
    var imageName: String? = null,
    var imageUrl: String? = null,
    var timeCreate: Date? = null
)