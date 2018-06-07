package com.skywalker.core.conf

import com.skywalker.auth.utils.JwtTokenUtil
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptorAdapter
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.util.*


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val JwtTokenUtil: JwtTokenUtil
) :
    WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/socket").withSockJS()
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptorAdapter() {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                val accessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return message
                if (StompCommand.CONNECT == accessor.command) {
                    val authToken = accessor.getFirstNativeHeader("authorization")
                    val principal = JwtTokenUtil.getUserIdFromToken(authToken)
                    if (Objects.isNull(principal))
                        return message
                    accessor.user = principal
                }
                return message
            }
        })
    }
}
