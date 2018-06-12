package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler", "fieldHandler"])
data class MhoSkywalkerTravelNotesMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var messageId: Long = 0L,
    var travelNotesId: Long = 0L,
    var parentMessageId: Long? = null,
    var userId: Long = 0L,
    var content: String? = null,
    var timeCreate: Date = Date(),
    @ManyToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var user: SkywalkerUser? = null,
    @OneToMany(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "parentMessageId")
    var list: List<MhoSkywalkerTravelNotesMessage>? = emptyList()
)