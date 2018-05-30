package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class MhoSkywalkerActiveImage {
    @Id
    @GeneratedValue
    var activeImageId: Long = 0
    var activeId: Long = 0
    var imageName: String? = null
    var imageUrl: String? = null
    var timeCreate: Date? = null
    var isDelete: String? = null

}