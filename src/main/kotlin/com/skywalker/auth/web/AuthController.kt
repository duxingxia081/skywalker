package com.skywalker.auth.web

import com.skywalker.auth.handler.TokenHandler
import com.skywalker.auth.service.SecurityContextService
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
        private val authenticationManager: AuthenticationManager,
        private val tokenHandler: TokenHandler,
        private val securityContextService: SecurityContextService
) {
    @PostMapping
    fun auth(@RequestBody params: AuthParams): AuthResponse {
        try {
            val loginToken = UsernamePasswordAuthenticationToken(params.userName, params.password)
            val authentication = authenticationManager.authenticate(loginToken)
            SecurityContextHolder.getContext().authentication = authentication

            return securityContextService.currentUser()
                    .let { requireNotNull(it) }
                    .let { tokenHandler.createTokenForUser(it).let(::AuthResponse) }
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1101,ErrorConstants.ERROR_MSG_1101)
        }
    }

    data class AuthParams(
            val userName: String,
            val password: String
    )

    data class AuthResponse(val token: String)

}
