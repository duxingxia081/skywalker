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
        var startAddressName: String?,
        var startAddressCoordinate: String?,
        @field:NotBlank(message = "目的地不能为空")
        var endAddressName: String?,
        var endAddressCoordinate: String?,
        var goTime: Date? = Date(),
        @field:NotNull(message = "活动耗时不能为空")
        var days: Long?,
        @field:NotBlank(message = "费用不能为空")
        var charge: String?,
        @field:NotBlank(message = "活动内容不能为空")
        var content: String?,
        var coverImage: String?,
        var userName: String?,
        var nickname: String?,
        var headImage: String?,
        var typeName: String?
) {
    var listActiveUserDTO: List<ActiveUserDTO>? = null
    var listActiveImgDTO: List<ActiveImgDTO>? = null
}