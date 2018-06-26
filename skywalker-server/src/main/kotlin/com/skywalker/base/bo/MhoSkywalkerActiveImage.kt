package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
class MhoSkywalkerActiveImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var activeImageId: Long = 0
    var activeId: Long = 0
    var imageName: String? = null
    var imageUrl: String? = null
    @Temporal(TemporalType.TIMESTAMP)
    var timeCreate: Date? = null
    var isDelete: String? = null

}