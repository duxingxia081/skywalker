package com.skywalker.auth.service.impl

import com.skywalker.auth.service.SecurityContextService
import com.skywalker.base.bo.MhoSkywalkerUser
import com.skywalker.user.service.impl.UserDetailsImpl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityContextServiceImpl : SecurityContextService {
    override fun currentUser(): MhoSkywalkerUser? {
        return SecurityContextHolder
            .getContext()
            .authentication
            .principal
            .let {
                when (it) {
                    is UserDetailsImpl -> it.mhoSkywalkerUser
                    else -> null
                }
            }
    }

}
