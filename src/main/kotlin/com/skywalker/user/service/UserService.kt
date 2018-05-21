package com.skywalker.user.service

import com.skywalker.user.dto.SkywalkerUserDTO

interface UserService {
    fun create(params: SkywalkerUserDTO): SkywalkerUserDTO
    fun findByUserName(userName: String): SkywalkerUserDTO
}