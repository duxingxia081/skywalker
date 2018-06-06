package com.skywalker.core.web

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class WebSocketController(
    private val messagingTemplate: SimpMessagingTemplate
) {
    @RequestMapping("/ws")
    fun index(): String {
        return "ws"
    }

    @MessageMapping(value = "/chat")
    private fun myInfo(msg: String) {
        messagingTemplate.convertAndSend(
            "/topic/notifications", msg
        )
    }

    @RequestMapping(value = "/test")
    private fun test(): String {
        messagingTemplate.convertAndSend(
            "/topic/notifications", "测试"
        )
        return "hello"
    }
}