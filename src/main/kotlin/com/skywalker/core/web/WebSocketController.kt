package com.skywalker.core.web

import com.skywalker.core.response.ServerMessage
import com.skywalker.core.utils.BaseTools
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal
import javax.servlet.http.HttpServletRequest

@Controller
class WebSocketController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val userRegistry: SimpUserRegistry,
    private val baseTools: BaseTools
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
    private fun test(request: HttpServletRequest): String {
        baseTools.convertAndSendToUser("weizh",
            ServerMessage("活动留言", userRegistry.getUser("weizh")?.name + userRegistry.userCount)
        )
        return "hello"
    }
}