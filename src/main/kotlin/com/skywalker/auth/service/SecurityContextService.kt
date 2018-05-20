package com.skywalker.auth.service

import com.skywalker.base.bo.MhoSkywalkerUser

interface SecurityContextService {
    fun currentUser(): MhoSkywalkerUser?
}
