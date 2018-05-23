package com.skywalker.auth.web

import com.skywalker.auth.handler.TokenHandler
import com.skywalker.auth.service.SecurityContextService
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val tokenHandler: TokenHandler,
    private val securityContextService: SecurityContextService
) {
    @PostMapping
    fun auth(@Valid @RequestBody params: AuthParams, result: BindingResult): AuthResponse {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.getFieldErrors())
        }
        val loginToken = UsernamePasswordAuthenticationToken(params.userName, params.password)
        val authentication = authenticationManager.authenticate(loginToken)
        SecurityContextHolder.getContext().authentication = authentication

        return securityContextService.currentUser()
            .let { requireNotNull(it) }
            .let { tokenHandler.createTokenForUser(it).let(::AuthResponse) }
    }

    data class AuthParams(
        @field:NotBlank(message = "密码不能为空")
        val userName: String,
        @field:NotBlank(message = "密码不能为空")
        val password: String
    )

    data class AuthResponse(val token: String)

}
