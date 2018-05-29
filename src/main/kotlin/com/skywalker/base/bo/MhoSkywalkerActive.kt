package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerActive(
        @Id
        @GeneratedValue
        var activeId: Long = 0,
        var activeTitle: String = "",
        var postUserId: Long = 0,
        var typeId: Long = 0,
        var startAddressName: String? = "",
        var startAddressCoordinate: String? = "",
        var endAddressName: String? = "",
        var endAddressCoordinate: String? = "",
        var goTime: Date? = Date(),
        var days: Long? = 0,
        var charge: String? = "",
        var content: String? = "",
        var coverImage: String? = "",
        var timeCreate: Date? = Date(),
        var isDelete: String? = ""
)