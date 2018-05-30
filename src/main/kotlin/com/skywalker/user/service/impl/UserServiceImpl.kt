package com.skywalker.user.service.impl

import com.skywalker.base.bo.MhoSkywalkerUser
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.dto.UserDTO
import com.skywalker.user.repository.UserRepository
import com.skywalker.user.service.UserService
import org.springframework.beans.BeanUtils
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.validation.Validator

@Service
@Transactional
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val validator: Validator
) : UserService {
    @Transactional(propagation = Propagation.REQUIRED)
    override fun create(userDto: SkywalkerUserDTO): SkywalkerUserDTO {
        validate(userDto)
        var user = userRepository.findByUserName(userDto.userName)
        if (null != user)
            throw ServiceException(ErrorConstants.ERROR_CODE_1102, ErrorConstants.ERROR_MSG_1102)
        try {
            user = MhoSkywalkerUser()
            BeanUtils.copyProperties(userDto, user)
            user.password = encrypt(user.password)?:""
            userRepository.save(user)
        } catch(e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1103, ErrorConstants.ERROR_MSG_1103,e)
        }
        return userDto
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun update(userDto: SkywalkerUserDTO): SkywalkerUserDTO {
        var user = userRepository.getOne(userDto.userId)
        try {
            userRepository.save(user.copy(nickname = userDto.nickname,
                    sign = userDto.sign,
                    headImage = userDto.headImage?:user.headImage,
                    password = encrypt(userDto.password)?:user.password))
        } catch(e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1108, ErrorConstants.ERROR_MSG_1108,e)
        }
        return userDto
    }
    @Transactional(readOnly = true)
    override fun findByUserName(userName: String): SkywalkerUserDTO? {
        val user: MhoSkywalkerUser? = userRepository.findByUserName(userName)
        if (null != user) {
            var dto = SkywalkerUserDTO()
            BeanUtils.copyProperties(user, dto)
            return dto
        }
        return null
    }

    @Transactional(readOnly = true)
    override fun findById(userId: Long): UserDTO {
        val user = userRepository.getOne(userId) ?: throw ServiceException(ErrorConstants.ERROR_CODE_1105, ErrorConstants.ERROR_MSG_1105)
        var dto = UserDTO()
        BeanUtils.copyProperties(user, dto)
        return dto
    }

    private fun validate(user: SkywalkerUserDTO) = validator.validate(user).apply {
        if (isNotEmpty()) throw DataIntegrityViolationException(toString())
    }

    private fun encrypt(secret: String?): String? {
        if(null==secret)
            return null
        else
        {
            return BCryptPasswordEncoder().encode(secret)
        }
    }
}