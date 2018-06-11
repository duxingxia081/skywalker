package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerActive(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var activeId: Long = 0,
        var activeTitle: String = "",
        var postUserId: Long ? = null,
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
        var timeCreate: Date? = null
)