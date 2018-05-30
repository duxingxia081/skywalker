package com.skywalker.active.service

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.active.dto.ActiveLeaveMessageDTO
import com.skywalker.active.repository.ActiveImgRepository
import com.skywalker.active.repository.ActiveLeaveMessageRepository
import com.skywalker.active.repository.ActiveRepository
import com.skywalker.active.repository.ActiveUserRepository
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils

@Service
class ActiveService(
    private val activeRepository: ActiveRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val activeImgRepository: ActiveImgRepository,
    private val activeLeaveMessageRepository: ActiveLeaveMessageRepository
) {
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
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110,e)
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
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110,e)
        }
    }
    @Transactional(readOnly = true)
    fun listActiveMsgByActiveId(activeId: Long?, pageable: Pageable): Page<ActiveLeaveMessageDTO>? {
        if (null == activeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            val list = activeLeaveMessageRepository.listAllByActiveId(activeId, pageable)
            return list
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110,e)
        }
    }
}