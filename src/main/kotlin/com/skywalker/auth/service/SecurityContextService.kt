package com.myapp.auth

import com.skywalker.base.bo.MhoSkywalkerUser

interface SecurityContextService {
    fun currentUser(): MhoSkywalkerUser?
}
