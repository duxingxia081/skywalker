package com.skywalker.user.dto

import java.util.*

data class SkywalkerUserDTO(
        val userId: Long=0L,
        val userName: String="",
        val mobilePhone: String="",
        val wechatId: String="",
        val qqId: String="",
        val password: String="",
        val nickname: String="",
        val sign: String="",
        val sex: String="",
        val address: String="",
        val qrCodeImage: String="",
        val headImage: String="",
        val coverImage: String="",
        val timeCreate: Date=Date(),
        val isDelete: String=""
)