package com.skywalker.core.conf

import com.skywalker.auth.service.TokenAuthenticationService
import com.skywalker.auth.utils.JwtTokenUtil
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptorAdapter
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.core.context.SecurityContextHolder
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
    private val log = LoggerFactory.getLogger(WebSocketConfig::class.java)
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

                    //                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    //                    String name = auth.getName(); //get logged in username
                    //                    System.out.println("Authenticated User : " + name);

                    val authToken = accessor.getFirstNativeHeader("authorization")
                    val principal = JwtTokenUtil.getUserIdFromToken(authToken)
                    if (Objects.isNull(principal))
                        return message
                    accessor.user = principal
                }/* else if (StompCommand.DISCONNECT == accessor.getCommand()) {
                    val authentication = SecurityContextHolder.getContext().authentication

                    if (Objects.nonNull(authentication))
                        log.info("Disconnected Auth : " + authentication.name)
                    else
                        log.info("Disconnected Sess : " + accessor.getSessionId())
                }*/
                return message
            }
/*
            override fun postSend(message: Message<*>, channel: MessageChannel, sent: Boolean) {
                val sha = StompHeaderAccessor.wrap(message)

                // ignore non-STOMP messages like heartbeat messages
                if (sha.command == null) {
                    log.warn("postSend null command")
                    return
                }

                val sessionId = sha.sessionId

                when (sha.command) {
                    StompCommand.CONNECT -> log.info("STOMP Connect [sessionId: $sessionId]")
                    StompCommand.CONNECTED -> log.info("STOMP Connected [sessionId: $sessionId]")
                    StompCommand.DISCONNECT -> log.info("STOMP Disconnect [sessionId: $sessionId]")
                    else -> {
                    }
                }
            }*/
        })

    }
}
