package com.skywalker.user.service.impl

import com.skywalker.base.bo.UserDetailsImpl
import com.skywalker.user.repository.UserRepository
import org.springframework.security.authentication.AccountStatusUserDetailsChecker
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(userName: String): UserDetailsImpl =
        userRepository
            .findByUserName(userName)
            ?.let(::UserDetailsImpl)
            ?.apply {
                AccountStatusUserDetailsChecker().check(this)
            } ?: throw UsernameNotFoundException("user not found.")

}

