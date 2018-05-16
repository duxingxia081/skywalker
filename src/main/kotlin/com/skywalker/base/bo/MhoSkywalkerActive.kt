package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerActive(
        @Id
        @GeneratedValue
        var activeId: Long,
        var activeTitle: String,
        var postUserId: Long,
        var startAddressName: String,
        var startAddressCoordinate: String,
        var endAddressName: String,
        var endAddressCoordinate: String,
        var goTime: Date,
        var days: Long,
        var charge: String,
        var content: String,
        var coverImage: String,
        var timeCreate: Date,
        var isDelete: String
)