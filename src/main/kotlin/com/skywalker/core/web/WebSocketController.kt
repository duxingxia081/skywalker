package com.skywalker.core.web

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
class WebSocketController(
    private val messagingTemplate: SimpMessagingTemplate
) {
    @RequestMapping("/ws")
    fun index(): String {
        return "ws"
    }

    @PostMapping("/ws2")
    fun index1(): String {
        return "ws"
    }

    @MessageMapping(value = "/chat")
    private fun myInfo(principal: Principal, msg: String) {
        println(principal.name)
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