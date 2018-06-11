package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerTravelNotesMessage(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var messageId: Long,
        var travelNotesId: Long,
        var parentMessageId: Long,
        var sendMessageUserId: Long,
        var content: String,
        var timeCreate: Date,
        var isDelete: String

)