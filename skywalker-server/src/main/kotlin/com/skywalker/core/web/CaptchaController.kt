package com.skywalker.core.web

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.core.codec.Base64
import cn.hutool.extra.qrcode.QrCodeUtil
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.SuccessResponse
import com.skywalker.user.dto.UserDTO
import com.skywalker.user.service.UserService
import org.springframework.web.bind.annotation.*
import java.awt.Color
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class CaptchaController(private val jwtTokenUtil: JwtTokenUtil,
                        private val userService: UserService) {
    @GetMapping("/captcha")
    fun getKaptcha(response: HttpServletResponse, request: HttpServletRequest): SuccessResponse {
        //定义图形验证码的长和宽
        val lineCaptcha = CaptchaUtil.createCircleCaptcha(100, 30, 4, 10)
        lineCaptcha.setBackground(Color(220, 106, 169))
        request.session.setAttribute("captcha", lineCaptcha.code)
        return SuccessResponse("data:image/gif;base64," + lineCaptcha.imageBase64)
    }

    @GetMapping("/qrCode")
    fun getQrCode(response: HttpServletResponse, request: HttpServletRequest): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        val user: UserDTO = userService.findById(userId)
        return SuccessResponse("data:image/gif;base64," + Base64.encode(QrCodeUtil.generatePng(user.userName, 300, 300)))
    }
}
