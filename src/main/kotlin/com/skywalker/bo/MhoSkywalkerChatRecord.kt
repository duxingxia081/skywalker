package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerChatRecord(

        @Id
        @GeneratedValue
        var recordId: Long,
        var content: String,
        var recordTime: Date,
        var sendUserId: Long,
        var roomId: Long,
        var timeCreate: Date,
        var isDelete: String
)