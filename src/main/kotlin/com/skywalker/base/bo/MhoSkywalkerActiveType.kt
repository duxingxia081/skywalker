package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerActiveType(
        @Id
        @GeneratedValue
        var typeId: Long = 0L,
        var typeName: String = "",
        var typeImage: String = "",
        var timeCreate: Date? = null,
        var isDelete: String? = null
)