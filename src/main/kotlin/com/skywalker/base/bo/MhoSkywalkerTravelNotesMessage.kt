package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerTravelNotesMessage(
        @Id
        @GeneratedValue
        var messageId: Long,
        var travelNotesId: Long,
        var parentMessageId: Long,
        var sendMessageUserId: Long,
        var content: String,
        var timeCreate: Date,
        var isDelete: String

)