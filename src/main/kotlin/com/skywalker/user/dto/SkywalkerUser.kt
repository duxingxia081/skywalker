package com.skywalker.user.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class MhoSkywalkerUserDTO(
        val userId: Long,
        @get:JsonIgnore
        @get:Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
        @get:Size(min = 4, max = 30)
        val userName: String,
        val mobilePhone: String,
        val wechatId: String,
        val qqId: String,

        @get:JsonIgnore
        val password: String,

        @get:Size(min = 2, max = 30)
        val nickname: String,
        val sign: String,
        val sex: String,
        val address: String,
        val qrCodeImage: String,
        val headImage: String,
        val coverImage: String,
        val timeCreate: Date,
        val isDelete: String
)