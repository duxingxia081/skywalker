package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerActiveUser(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0L,
        var activeId: Long = 0L,
        var userId: Long = 0L,
        @Temporal(TemporalType.TIMESTAMP)
        var timeCreate: Date? = null,
        var isDelete: String? = null
)
