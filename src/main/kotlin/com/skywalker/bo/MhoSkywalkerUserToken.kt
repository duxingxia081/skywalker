package com.skywalker.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerUserToken(
        @Id
        @GeneratedValue
        var id: Long,
        var userId: Long,
        var userName: String,
        var accessToken: String,
        var timeCreate: Date,
        var isDelete: String
)
