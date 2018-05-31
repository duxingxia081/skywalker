package com.skywalker.active.dto

import java.util.*

data class ActiveLeaveMessageDTO(
        val userName: String?,
        val nickname: String?,
        val headImage: String?,
        val content: String? = null,
        val timeCreate: Date? = null,
        val parenUserName: String?,
        val parenNickname: String?,
        val parenHeadImage: String?,
        val parenContent: String? = null,
        val parenTimeCreate: Date? = null
)