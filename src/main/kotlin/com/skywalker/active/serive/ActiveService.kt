package com.skywalker.active.service

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.active.repository.ActiveRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActiveService(
        private val activeRepository: ActiveRepository
) {
    @Transactional(readOnly = true)
    fun listAllByTypeId(typeId: Long, pageable: Pageable): Page<ActiveDTO>? {
        val list = activeRepository.listAllByTypeId(typeId,pageable)
        return list
    }
}