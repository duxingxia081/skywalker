package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerChatRoomUser(

        @Id
        @GeneratedValue
        var id: Long,
        var roomId: Long,
        var userId: Long,
        var timeCreate: Date,
        var isDelete: String

)