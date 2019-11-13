package com.skywalker.auth.web

import cn.hutool.http.HttpRequest
import com.skywalker.auth.handler.TokenHandler
import com.skywalker.auth.service.SecurityContextService
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.SuccessResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotBlank


@RestController
@RequestMapping("/wx")
class WeChatController(
        private val authenticationManager: AuthenticationManager,
        private val tokenHandler: TokenHandler,
        private val securityContextService: SecurityContextService
) {
    @Value("\${wx.appid}")
    private val appid: String = ""
    @Value("\${wx.access.token.url}")
    private val accessTokenUrl: String = ""
    @Value("\${wx.grant.type}")
    private val grantType: String = ""
    @Value("\${wx.app.secret}")
    private val appSecret: String = ""
    @RequestMapping("/wxlogin")
    @ResponseBody
    fun login(code:String): SuccessResponse {
        try {
            if(StringUtils.isEmpty(code))
            {
                throw ServiceException("not null")
            }
            val paramMap = mapOf("appid" to appid, "secret" to appSecret, "grant_type" to grantType, "js_code" to code)
            HttpRequest.get(accessTokenUrl).form(paramMap).execute().body();
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
            val password: String
            /*@field:NotBlank(message = "验证码不能为空")
            val captcha: String*/
    )
}
