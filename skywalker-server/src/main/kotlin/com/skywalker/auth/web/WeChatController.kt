package com.skywalker.auth.web

import cn.hutool.http.HttpRequest
import cn.hutool.json.JSONObject
import cn.hutool.json.JSONUtil
import com.skywalker.auth.handler.TokenHandler
import com.skywalker.auth.service.SecurityContextService
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.SuccessResponse
import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.service.UserService
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
        private val userService: UserService,
        private val tokenHandler: TokenHandler,
        private val securityContextService: SecurityContextService
) {
    @Value("\${wx.appid}")
    private val appid: String = ""
    @Value("\${wx.jscode.session.url}")
    private val jscode2session: String = ""
    @Value("\${wx.grant.type}")
    private val grantType: String = ""
    @Value("\${wx.app.secret}")
    private val appSecret: String = ""
    @RequestMapping("/wxlogin")
    @ResponseBody
    fun login(code:String,nickname:String): SuccessResponse {
        try {
            if(StringUtils.isEmpty(code))
            {
                throw ServiceException("not null")
            }
            val paramMap = mapOf("appid" to appid, "secret" to appSecret, "grant_type" to grantType, "js_code" to code)
            var jsonData:String = HttpRequest.get(jscode2session).form(paramMap).execute().body();
           var jsonObject:JSONObject =  JSONUtil.parseObj(jsonData)
            val openid = jsonObject["openid"]
            val sessionKey = jsonObject["session_key"]
            if(null==openid||null==sessionKey)
            {
                throw  ServiceException("获取用户信息失败");
            }
            val skywalkerUserDTO = userService.createOrGet(params = SkywalkerUserDTO(userName = openid.toString(),nickname = nickname,password = openid.toString()))
            println(skywalkerUserDTO)
            return  SuccessResponse(skywalkerUserDTO);
        } catch (e: BadCredentialsException) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1101, ErrorConstants.ERROR_MSG_1101, e)
        } catch (e: ServiceException) {
            throw e
        }catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1109, ErrorConstants.ERROR_MSG_1109, e)
        }
        return  SuccessResponse("ddd");
    }
}
