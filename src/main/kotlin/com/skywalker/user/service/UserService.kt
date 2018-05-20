package com.skywalker.user.service

import com.skywalker.user.dto.MhoSkywalkerUserDTO

interface UserService {
    fun create(params: MhoSkywalkerUserDTO): MhoSkywalkerUserDTO
}