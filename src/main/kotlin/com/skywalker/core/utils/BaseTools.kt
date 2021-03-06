package com.skywalker.core.utils

import com.skywalker.core.response.ServerMessage
import com.skywalker.user.service.UserService
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class BaseTools(
    private val messagingTemplate: SimpMessagingTemplate,
    private val userService: UserService
) {
    /**
     * 发送消息
     */
    fun convertAndSendToUser(userName: String, serverMessage: ServerMessage) {
        messagingTemplate.convertAndSendToUser(
            userName,
            "/topic/message", serverMessage
        )
    }

    /**
     * 发送消息
     */
    fun convertAndSendToUser(userId: Long, serverMessage: ServerMessage) {
        val userName = getUserNameByUserId(userId) ?: return
        messagingTemplate.convertAndSendToUser(
            userName,
            "/topic/message", serverMessage
        )
    }

    /**
     * 根据用户id获取用户名
     */
    fun getUserNameByUserId(userId: Long): String? {
        return userService.findById(userId).userName
    }
}