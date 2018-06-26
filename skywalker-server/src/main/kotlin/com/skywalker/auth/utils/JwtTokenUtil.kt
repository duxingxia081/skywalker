package com.skywalker.auth.utils

import com.skywalker.auth.authentication.UserAuthentication
import com.skywalker.auth.handler.TokenHandler
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenUtil(
    val tokenHandler: TokenHandler
) {
    @Value("\${app.jwt.secret}")
    private val secret: String? = null

    fun getUserIdFromToken(request: HttpServletRequest): Long? {
        val authHeader = request.getHeader("authorization") ?: return null
        if (!authHeader.startsWith("Bearer")) return null

        val token = authHeader.substring(7)
        if (token.isEmpty()) return null
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
            .subject.toLong()
    }

    fun getUserIdFromToken(authHeader: String?): Authentication? {
        if (null == authHeader || !authHeader.startsWith("Bearer")) return null
        val token = authHeader.substring(7)
        if (token.isEmpty()) return null
        return tokenHandler
            .parseUserFromToken(token)
            .let(::UserAuthentication)
    }

    fun getUserFromToken(request: HttpServletRequest): Authentication? {
        val authHeader = request.getHeader("authorization") ?: return null
        if (!authHeader.startsWith("Bearer")) return null
        val token = authHeader.substring(7)
        if (token.isEmpty()) return null
        return tokenHandler
            .parseUserFromToken(token)
            .let(::UserAuthentication)
    }
}