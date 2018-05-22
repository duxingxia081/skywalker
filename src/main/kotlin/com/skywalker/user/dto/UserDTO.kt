package com.skywalker.user.dto

import java.util.*

data class UserDTO(
        var userName: String?="",
        var mobilePhone: String?="",
        var wechatId: String?="",
        var qqId: String?="",
        var nickname: String?="",
        var sign: String?="",
        var sex: String?="",
        var address: String?="",
        var qrCodeImage: String?="",
        var headImage: String?="",
        var coverImage: String?="",
        var timeCreate: Date=Date(),
        var isDelete: String=""
)