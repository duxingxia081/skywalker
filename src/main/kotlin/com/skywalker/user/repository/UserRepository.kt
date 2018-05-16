package com.skywalker.user.repository

import com.skywalker.base.bo.MhoSkywalkerUser
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<MhoSkywalkerUser, Long> {
    fun findByUserId(userId: String): List<MhoSkywalkerUser>
}