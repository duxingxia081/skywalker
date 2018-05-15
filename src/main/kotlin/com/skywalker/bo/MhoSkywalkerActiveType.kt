package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerActiveType(
        @Id
        @GeneratedValue
        var typeId: Long,
        var typeName: String,
        var typeImage: String,
        var timeCreate: Date,
        var isDelete: String
)