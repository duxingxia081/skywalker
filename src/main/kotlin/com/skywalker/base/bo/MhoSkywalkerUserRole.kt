package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerUserRole(

        @Id
        @GeneratedValue
        var id: Long,
        var userId: Long,
        var roleId: Long,
        var timeCreate: Date,
        var isDelete: String
)