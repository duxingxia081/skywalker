package com.skywalker.auth.service

import com.skywalker.auth.authentication.UserAuthentication
import com.skywalker.auth.handler.TokenHandler
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class TokenAuthenticationService(private val tokenHandler: TokenHandler) {
    val logger = LoggerFactory.getLogger(TokenAuthenticationService::class.java)
    fun authentication(request: HttpServletRequest): Authentication? {
        logger.info("dddddddddddddddddd")
        val authHeader = request.getHeader("authorization") ?: return null
        if (!authHeader.startsWith("Bearer")) return null

        val jwt = authHeader.substring(7)
        if (jwt.isEmpty()) return null

        return tokenHandler
                .parseUserFromToken(jwt)
                .let(::UserAuthentication)
    }
}
