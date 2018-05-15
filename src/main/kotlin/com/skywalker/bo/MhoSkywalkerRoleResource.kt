package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerRoleResource(
        @Id
        @GeneratedValue
        var id: Long,
        var roleId: Long,
        var resourceId: Long,
        var timeCreate: Date,
        var isDelete: String
)
