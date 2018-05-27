package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerUser constructor(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var userId: Long=0L,
        var userName: String="",
        var mobilePhone: String?="",
        var wechatId: String?="",
        var qqId: String?="",
        var password: String="",
        var nickname: String?="",
        var sign: String?="",
        var sex: String?="",
        var address: String?="",
        var qrCodeImage: String?="",
        var headImage: String?="",
        var coverImage: String?="",
        var timeCreate: Date?=Date(),
        var isDelete: String?="N"
)