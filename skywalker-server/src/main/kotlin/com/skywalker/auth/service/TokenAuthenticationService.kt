package com.skywalker.auth.service

import org.springframework.security.core.Authentication

import javax.servlet.http.HttpServletRequest

interface TokenAuthenticationService {
    fun authentication(request: HttpServletRequest): Authentication?
}

