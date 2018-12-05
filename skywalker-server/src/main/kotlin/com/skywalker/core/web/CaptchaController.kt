package com.skywalker.core.web

import cn.hutool.captcha.CaptchaUtil
import com.skywalker.core.response.SuccessResponse
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.awt.Color
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/captcha")
@CrossOrigin
class CaptchaController {
    @GetMapping
    fun getKaptcha(response: HttpServletResponse, request: HttpServletRequest): SuccessResponse {
        //定义图形验证码的长和宽
        val lineCaptcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 50)
        lineCaptcha.setBackground(Color(220, 106, 169))
        request.session.setAttribute("captcha",lineCaptcha.code)
        return SuccessResponse("data:image/gif;base64,"+lineCaptcha.imageBase64)
    }
}