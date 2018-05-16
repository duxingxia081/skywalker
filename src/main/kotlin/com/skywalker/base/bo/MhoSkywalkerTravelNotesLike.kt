package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerTravelNotesLike(

        @Id
        @GeneratedValue
        var likeId: Long,
        var travelNotesId: Long,
        var dolikeUserId: Long,
        var timeCreate: Date,
        var isDelete: String

)