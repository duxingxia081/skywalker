package com.skywalker.active.dto

import java.util.*

data class ActiveDTO(
        val activeId: Long,
        val activeTitle: String,
        val postUserId: Long,
        val startAddressName: String,
        val startAddressCoordinate: String,
        val endAddressName: String,
        val endAddressCoordinate: String,
        val goTime: Date,
        val days: Long,
        val charge: String,
        val content: String,
        val coverImage: String
)
{
        private var total: Long = 0
}