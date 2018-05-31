package com.skywalker.base.bo

import java.util.*
import javax.persistence.*

@Entity
class MhoSkywalkerActiveImage {
    @Id
    @GeneratedValue
    var activeImageId: Long = 0
    var activeId: Long = 0
    var imageName: String? = null
    var imageUrl: String? = null
    @Temporal(TemporalType.TIMESTAMP)
    var timeCreate: Date? = null
    var isDelete: String? = null

}