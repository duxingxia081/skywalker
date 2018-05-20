package com.myapp.service

import com.skywalker.base.bo.MhoSkywalkerUser
import com.skywalker.user.dto.MhoSkywalkerUserDTO
import com.skywalker.user.repository.UserRepository
import com.skywalker.user.service.UserService
import org.springframework.beans.BeanUtils
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Validator

@Service
@Transactional
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val validator: Validator
) : UserService{
    override fun create(userDto: MhoSkywalkerUserDTO): MhoSkywalkerUserDTO {
        validate(userDto)
        var user: MhoSkywalkerUser  = MhoSkywalkerUser();
        BeanUtils.copyProperties(userDto,user)
        user.password=encrypt(user.password)
        userRepository.save(user)
        return userDto
    }
    private fun validate(user: MhoSkywalkerUserDTO) = validator.validate(user).apply {
        if (isNotEmpty()) throw DataIntegrityViolationException(toString())
    }
    private fun encrypt(secret: String) = BCryptPasswordEncoder().encode(secret)
}