package com.skywalker.user.dto

import org.hibernate.validator.constraints.Length
import java.util.*
import javax.validation.constraints.NotBlank

data class SkywalkerUserDTO(
    var userId: Long = 0L,
    @field:NotBlank(message = "用户名不能为空")
    var userName: String? = "",
    var mobilePhone: String? = "",
    var wechatId: String? = "",
    var qqId: String? = "",
    @field:Length(min = 6, max = 16, message = "密码必须在6-16位之间")
    var password: String? = "",
    var nickname: String? = "",
    var sign: String? = "",
    var sex: String? = "",
    var address: String? = "",
    var qrCodeImage: String? = "",
    var headImage: String? = "",
    var coverImage: String? = "",
    var timeCreate: Date = Date(),
    var isDelete: String = ""
)