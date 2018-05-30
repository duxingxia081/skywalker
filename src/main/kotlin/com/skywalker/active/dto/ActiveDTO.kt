package com.skywalker.active.dto

import java.util.*

data class ActiveDTO(
    val activeId: Long,
    val activeTitle: String,
    val postUserId: Long,
    val typeId: Long,
    val startAddressName: String?,
    val startAddressCoordinate: String?,
    val endAddressName: String?,
    val endAddressCoordinate: String?,
    val goTime: Date? = Date(),
    val days: Long?,
    val charge: String?,
    val content: String?,
    val coverImage: String?,
    val userName: String?,
    val nickname: String?,
    val headImage: String?,
    val typeName: String?
) {
    var listActiveUserDTO: List<ActiveUserDTO>? = null
    var listActiveImgDTO: List<ActiveImgDTO>? = null
}