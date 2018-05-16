package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerChatRoom(

        @Id
        @GeneratedValue
        var roomId: Long,
        var roomName: String,
        var roomType: String,
        var timeCreate: Date,
        var isDelete: String
)