package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerRole(

        @Id
        @GeneratedValue
        var roleId: Long,
        var roleName: String,
        var timeCreate: Date,
        var isDelete: String
)