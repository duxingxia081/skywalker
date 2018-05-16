package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerResource(
        @Id
        @GeneratedValue
        var resourceId: Long,
        var resourceName: String,
        var resourceUrl: String,
        var resourceMethod: String,
        var timeCreate: Date,
        var isDelete: String

)