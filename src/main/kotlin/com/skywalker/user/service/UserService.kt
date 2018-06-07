package com.skywalker.user.service

import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.dto.UserDTO

interface UserService {
    fun create(params: SkywalkerUserDTO): SkywalkerUserDTO
    fun update(params: SkywalkerUserDTO): SkywalkerUserDTO?
    fun findByUserName(userName: String): SkywalkerUserDTO?
    fun findById(userId: Long): UserDTO
    fun updateHead(userId: Long, headImg: String): String
}