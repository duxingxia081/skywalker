package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerActiveUser(
        @Id
        @GeneratedValue
        var id: Long,
        var activeId: Long,
        var userId: Long,
        var timeCreate: Date,
        var isDelete: String
)
