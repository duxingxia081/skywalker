package com.skywalker.core.web

import cn.hutool.captcha.CaptchaUtil
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.awt.Color
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/kaptcha")
@CrossOrigin
class KaptchaAction {
    @GetMapping
    fun getKaptcha(response: HttpServletResponse):String {
        //定义图形验证码的长和宽
        val lineCaptcha = CaptchaUtil.createCircleCaptcha(200, 100,4,50)
        lineCaptcha.setBackground(Color(220, 106, 169))
        return lineCaptcha.imageBase64
    }
}