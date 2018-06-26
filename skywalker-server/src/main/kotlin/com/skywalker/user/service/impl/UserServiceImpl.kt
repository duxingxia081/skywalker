package com.skywalker.user.service.impl

import com.skywalker.base.bo.MhoSkywalkerUser
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.dto.UserDTO
import com.skywalker.user.repository.UserRepository
import com.skywalker.user.service.UserService
import org.springframework.beans.BeanUtils
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    @Transactional
    override fun create(userDto: SkywalkerUserDTO): SkywalkerUserDTO {
        var user = userRepository.findByUserName(userDto.userName)
        if (null != user)
            throw ServiceException(ErrorConstants.ERROR_CODE_1102, ErrorConstants.ERROR_MSG_1102)
        try {
            user = MhoSkywalkerUser()
            BeanUtils.copyProperties(userDto, user)
            user.password = encrypt(user.password) ?: ""
            user.timeCreate = Date()
            userRepository.save(user)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1103, ErrorConstants.ERROR_MSG_1103, e)
        }
        return userDto
    }

    @Transactional
    override fun update(userDto: SkywalkerUserDTO): SkywalkerUserDTO {
        var user = userRepository.getOne(userDto.userId)
        try {
            userRepository.save(
                user.copy(
                    nickname = userDto.nickname ?: user.nickname,
                    sign = userDto.sign ?: user.sign,
                    headImage = userDto.headImage ?: user.headImage,
                    password = encrypt(userDto.password) ?: user.password,
                    address = userDto.address ?: user.address,
                    sex = userDto.sex ?: user.sex,
                    qqId = userDto.qqId ?: user.qqId,
                    wechatId = userDto.wechatId ?: user.wechatId,
                    mobilePhone = userDto.mobilePhone ?: user.mobilePhone
                )
            )
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1108, ErrorConstants.ERROR_MSG_1108, e)
        }
        return userDto
    }

    override fun findByUserName(userName: String): SkywalkerUserDTO? {
        val user: MhoSkywalkerUser? = userRepository.findByUserName(userName)
        if (null != user) {
            var dto = SkywalkerUserDTO()
            BeanUtils.copyProperties(user, dto)
            return dto
        }
        return null
    }

    override fun findById(userId: Long): UserDTO {
        val user = userRepository.getOne(userId)
        var dto = UserDTO()
        BeanUtils.copyProperties(user, dto)
        return dto
    }

    private fun encrypt(secret: String?): String? {
        return if (null == secret)
            null
        else {
            BCryptPasswordEncoder().encode(secret)
        }
    }

    @Transactional
    override fun updateHead(userId: Long, headImg: String): String {
        var user = userRepository.getOne(userId)
        try {
            userRepository.save(
                user.copy(
                    headImage = headImg
                )
            )
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1108, ErrorConstants.ERROR_MSG_1108, e)
        }
        return "修改成功"
    }
}