package com.skywalker.active.service

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.active.repository.ActiveImgRepository
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
    private val activeImgRepository: ActiveImgRepository
) {
    @Transactional(readOnly = true)
    fun listAllByTypeId(typeId: Long?, pageable: Pageable): Page<ActiveDTO>? {
        if (null == typeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        val list = activeRepository.listAllByTypeId(typeId, pageable)
        if (null != list && !CollectionUtils.isEmpty(list.content)) {
            for (active in list.content) {
                active.listActiveUserDTO = activeUserRepository.listAllByActiveId(active.activeId)
                active.listActiveImgDTO = activeImgRepository.listAllByActiveId(active.activeId)
            }
        }
        return list
    }
}