package com.skywalker.user.service

import com.skywalker.base.bo.MhoSkywalkerUser
import com.skywalker.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class UserService(val userRepository: UserRepository){
    @Transactional(readOnly = true)
    open fun findAll() : List<MhoSkywalkerUser> {
        return userRepository.findAll()
    }
}