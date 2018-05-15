package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerTravelNotesImage(
        @Id
        @GeneratedValue
        var imageId: Long,
        var travelNotesId: Long,
        var imageName: String,
        var imageUrl: String,
        var timeCreate: Date,
        var isDelete: String
)