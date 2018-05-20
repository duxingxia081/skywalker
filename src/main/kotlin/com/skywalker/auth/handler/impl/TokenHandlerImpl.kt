package com.myapp.auth

import com.myapp.domain.UserDetailsImpl
import com.skywalker.auth.handler.TokenHandler
import com.skywalker.base.bo.MhoSkywalkerUser
import com.skywalker.user.repository.UserRepository
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
class TokenHandlerImpl(
    @param:Value("\${app.jwt.secret}")
    private val secret: String,
    private val userRepository: UserRepository
) : TokenHandler {

    override fun parseUserFromToken(token: String): UserDetails {
        val userId = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
            .subject
            .toLong()

        return userRepository.findOne(userId).let(::UserDetailsImpl)
    }

    override fun createTokenForUser(user: MhoSkywalkerUser): String {
        val afterOneWeek = ZonedDateTime.now().plusWeeks(1)

        return Jwts.builder()
            .setSubject(user.userId.toString())
            .signWith(SignatureAlgorithm.HS512, secret)
            .setExpiration(Date.from(afterOneWeek.toInstant()))
            .compact()
    }

}

