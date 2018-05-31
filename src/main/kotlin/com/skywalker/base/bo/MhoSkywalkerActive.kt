package com.skywalker.base.bo

import java.util.*
import javax.persistence.*

@Entity
data class MhoSkywalkerActive(
        @Id
        @GeneratedValue
        var activeId: Long = 0,
        var activeTitle: String = "",
        var postUserId: Long = 0,
        var typeId: Long = 0,
        var startAddressName: String? = null,
        var startAddressCoordinate: String? = null,
        var endAddressName: String? = null,
        var endAddressCoordinate: String? = null,
        @Temporal(TemporalType.DATE)
        var goTime: Date? = null,
        var days: Long? = null,
        var charge: String? = null,
        var content: String? = null,
        var coverImage: String? = null,
        @Temporal(TemporalType.TIMESTAMP)
        var timeCreate: Date? = null,
        var isDelete: String? = null
)