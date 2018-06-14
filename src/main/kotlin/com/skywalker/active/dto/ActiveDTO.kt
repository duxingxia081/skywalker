package com.skywalker.active.dto

import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ActiveDTO(
        var activeId: Long = 0L,
        @field:NotBlank(message = "活动标题不能为空")
        var activeTitle: String = "",
        var postUserId: Long = 0L,
        @field:NotNull(message = "活动类型不能为空")
        var typeId: Long = 0L,
        @field:NotBlank(message = "出发地不能为空")
        var startAddressName: String? = null,
        var startAddressCoordinate: String? = null,
        @field:NotBlank(message = "目的地不能为空")
        var endAddressName: String? = null,
        var endAddressCoordinate: String? = null,
        var goTime: Date? = null,
        @field:NotNull(message = "活动耗时不能为空")
        var days: Long? = null,
        @field:NotBlank(message = "费用不能为空")
        var charge: String? = null,
        @field:NotBlank(message = "活动内容不能为空")
        var content: String? = null,
        var coverImage: String? = null,
        var userName: String? = null,
        var nickname: String? = null,
        var headImage: String? = null,
        var typeName: String? = null,
        var timeCreate: Date? = null
) {
    var listActiveUserDTO: List<ActiveUserDTO>? = null
    var listActiveImgDTO: List<ActiveImgDTO>? = null
}