package com.skywalker.base.bo

import java.util.*
import javax.persistence.*

@Entity
data class MhoSkywalkerActiveUser(
        @Id
        @GeneratedValue
        var id: Long,
        var activeId: Long,
        var userId: Long,
        @Temporal(TemporalType.TIMESTAMP)
        var timeCreate: Date,
        var isDelete: String
)
