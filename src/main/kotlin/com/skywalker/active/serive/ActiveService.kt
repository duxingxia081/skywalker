package com.skywalker.active.service

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.active.repository.ActiveImgRepository
import com.skywalker.active.repository.ActiveLeaveMessageRepository
import com.skywalker.active.repository.ActiveRepository
import com.skywalker.active.repository.ActiveUserRepository
import com.skywalker.base.bo.MhoSkywalkerActiveLeaveMessage
import com.skywalker.base.bo.MhoSkywalkerActiveUser
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import java.util.*

@Service
class ActiveService(
        private val activeRepository: ActiveRepository,
        private val activeUserRepository: ActiveUserRepository,
        private val activeImgRepository: ActiveImgRepository,
        private val activeLeaveMessageRepository: ActiveLeaveMessageRepository
) {
    @Transactional(propagation = Propagation.REQUIRED)
    fun create(activeId: Long, userId: Long): String {

        val bo = activeUserRepository.findByActiveIdAndUserId(activeId, userId)
        if (null != bo) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1111, ErrorConstants.ERROR_MSG_1111)
        }
        try {
            var user = MhoSkywalkerActiveUser()
            user.activeId = activeId!!
            user.userId = userId
            user.timeCreate = Date()
            activeUserRepository.save(user)
        } catch(e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
        }
        return "成功"
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun createMsg(activeId: Long, userId: Long, parentLeaveMessageId: Long?, content: String): String {
        try {
            var msg = MhoSkywalkerActiveLeaveMessage()
            msg.activeId = activeId
            msg.parentLeaveMessageId = parentLeaveMessageId
            msg.content = content
            msg.userId = userId
            msg.timeCreate = Date()
            activeLeaveMessageRepository.save(msg)
        } catch(e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
        }
        return "成功"
    }

    @Transactional(readOnly = true)
    fun listAllByTypeId(typeId: Long?, pageable: Pageable): Page<ActiveDTO>? {
        if (null == typeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            val list = activeRepository.listAllByTypeId(typeId, pageable)
            if (null != list && !CollectionUtils.isEmpty(list.content)) {
                for (active in list.content) {
                    active.listActiveUserDTO = activeUserRepository.listAllByActiveId(active.activeId)
                    active.listActiveImgDTO = activeImgRepository.listAllByActiveId(active.activeId)
                }
            }
            return list
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    @Transactional(readOnly = true)
    fun listAllByActiveId(activeId: Long?): ActiveDTO? {
        if (null == activeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            val activeDTO = activeRepository.listAllByActiveId(activeId)
            if (null != activeDTO) {
                activeDTO.listActiveUserDTO = activeUserRepository.listAllByActiveId(activeId)
                activeDTO.listActiveImgDTO = activeImgRepository.listAllByActiveId(activeId)
            }
            return activeDTO
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    @Transactional(readOnly = true)
    fun listActiveMsgByActiveId(activeId: Long?, pageable: Pageable): Page<MhoSkywalkerActiveLeaveMessage>? {
        if (null == activeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            return activeLeaveMessageRepository.listAllParentByActiveId(activeId, pageable)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }
}