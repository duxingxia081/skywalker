package com.skywalker.auth.utils

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenUtil{

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
}