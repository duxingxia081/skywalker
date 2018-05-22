package com.skywalker.auth.handler.impl

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AccessDeineHandlerImpl : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(p0: HttpServletRequest, p1: HttpServletResponse, p2: AccessDeniedException?) {
        print("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
        p1.setCharacterEncoding("utf-8")
        p1.setContentType("text/javascript;charset=utf-8")
        p1.getWriter().print("{\"ssss\":\"sss\"}")
    }
}