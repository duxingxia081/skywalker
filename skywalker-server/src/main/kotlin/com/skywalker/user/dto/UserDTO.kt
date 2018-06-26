package com.skywalker.user.dto

import java.util.*

data class UserDTO(
    var userId: Long? = null,
    var userName: String? = null,
    var mobilePhone: String? = null,
    var wechatId: String? = null,
    var qqId: String? = null,
    var nickname: String? = null,
    var sign: String? = null,
    var sex: String? = null,
    var address: String? = null,
    var qrCodeImage: String? = null,
    var headImage: String? = null,
    var coverImage: String? = null,
    var timeCreate: Date? = null
)