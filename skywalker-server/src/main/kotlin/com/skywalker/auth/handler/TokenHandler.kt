package com.skywalker.auth.handler

import com.skywalker.base.bo.MhoSkywalkerUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
interface TokenHandler {
    fun parseUserFromToken(token: String): UserDetails
    fun createTokenForUser(user: MhoSkywalkerUser): String
}