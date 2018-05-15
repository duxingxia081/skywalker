package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerWebsocketMessage(
        @Id
        @GeneratedValue
        var messageId: Long,
        var type: String,
        var typeId: Long,
        var fromUserId: Long,
        var toUserId: Long,
        var readed: String,
        var timeCreate: Date,
        var isDelete: String
)