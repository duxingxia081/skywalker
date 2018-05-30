package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerActiveLeaveMessage(
    @Id
    @GeneratedValue
    var leaveMessageId: Long = 0,
    var activeId: Long = 0,
    var userId: Long = 0,
    var content: String? = null,
    var timeCreate: Date? = null,
    var isDelete: String? = null
)