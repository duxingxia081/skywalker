package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerActiveLeaveMessage(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var leaveMessageId: Long = 0,
        var activeId: Long = 0,
        var userId: Long = 0,
        var parentLeaveMessageId: Long? = null,
        var content: String? = null,
        var timeCreate: Date? = null,
        @ManyToOne(cascade = arrayOf(CascadeType.ALL))
        @JoinColumn(name = "userId", insertable = false, updatable = false)
        var user: SkywalkerUser? = null,
        @OneToMany(cascade = arrayOf(CascadeType.ALL))
        @JoinColumn(name = "parentLeaveMessageId")
        var list: List<MhoSkywalkerActiveLeaveMessage>? = emptyList()
)