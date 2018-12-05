package com.skywalker.auth.web

import com.skywalker.auth.handler.TokenHandler
import com.skywalker.auth.service.SecurityContextService
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.SuccessResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
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
    fun auth(@Valid @RequestBody params: AuthParams, request: HttpServletRequest, result: BindingResult): SuccessResponse {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.fieldErrors)
        }
        val captcha = request.session.getAttribute("captcha")
        println(captcha)
        println(params.captcha)
        if (StringUtils.isEmpty(captcha) || captcha != params.captcha) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1115, ErrorConstants.ERROR_MSG_1115)
        }
        try {
            val loginToken = UsernamePasswordAuthenticationToken(params.userName, params.password)
            val authentication = authenticationManager.authenticate(loginToken)
            SecurityContextHolder.getContext().authentication = authentication

            return securityContextService.currentUser()
                    .let { requireNotNull(it) }
                    .let { tokenHandler.createTokenForUser(it).let(::SuccessResponse) }
        } catch (e: BadCredentialsException) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1101, ErrorConstants.ERROR_MSG_1101, e)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1109, ErrorConstants.ERROR_MSG_1109, e)
        }
    }

    data class AuthParams(
            @field:NotBlank(message = "用户名不能为空")
            val userName: String,
            @field:NotBlank(message = "密码不能为空")
            val password: String,
            @field:NotBlank(message = "验证码不能为空")
            val captcha: String
    )
}
