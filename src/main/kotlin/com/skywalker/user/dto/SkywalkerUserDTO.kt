package com.skywalker.user.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class SkywalkerUserDTO(
    var userId: Long = 0L,
    @field:NotBlank(message = "用户名不能为空")
    var userName: String? = null,
    var mobilePhone: String? = null,
    var wechatId: String? = null,
    var qqId: String? = null,
    @field:Length(min = 6, max = 16, message = "密码必须在6-16位之间")
    var password: String? = null,
    var nickname: String? = null,
    var sign: String? = null,
    var sex: String? = null,
    var address: String? = null,
    var qrCodeImage: String? = null,
    var headImage: String? = null,
    var coverImage: String? = null
)