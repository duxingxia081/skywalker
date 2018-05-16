package com.skywalker.base.bo

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MhoSkywalkerUser constructor(
        @Id
        @GeneratedValue
        var userId: Long=0L,
        var userName: String="",
        var mobilePhone: String="",
        var wechatId: String="",
        var qqId: String="",
        var password: String="",
        var nickname: String="",
        var sign: String="",
        var sex: String="",
        var address: String="",
        var qrCodeImage: String="",
        var headImage: String="",
        var coverImage: String="",
        var timeCreate: Date=Date(),
        var isDelete: String=""
)