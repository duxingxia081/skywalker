package com.skywalker.base.bo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@JsonIgnoreProperties(value = *arrayOf("hibernateLazyInitializer", "handler", "fieldHandler"))
data class MhoSkywalkerUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long = 0L,
    var userName: String = "",
    var mobilePhone: String? = null,
    var wechatId: String? = null,
    var qqId: String? = null,
    var password: String = "",
    var nickname: String? = null,
    var sign: String? = null,
    var sex: String? = null,
    var address: String? = null,
    var qrCodeImage: String? = null,
    var headImage: String? = null,
    var coverImage: String? = null,
    @Temporal(TemporalType.TIMESTAMP)
    var timeCreate: Date? = null,
    var isDelete: String? = null
)