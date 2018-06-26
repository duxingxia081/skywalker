package com.skywalker.base.bo

import java.util.*
import javax.persistence.*

@Entity
data class MhoSkywalkerActiveType(
        @Id
        @GeneratedValue
        var typeId: Long = 0L,
        var typeName: String = "",
        var typeImage: String = "",
        @Temporal(TemporalType.TIMESTAMP)
        var timeCreate: Date? = null,
        var isDelete: String? = null
)