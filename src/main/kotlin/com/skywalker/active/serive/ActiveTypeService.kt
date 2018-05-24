package com.skywalker.active.service

import com.skywalker.active.dto.ActiveTypeDTO
import com.skywalker.active.repository.ActiveTypeRepository
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActiveTypeService(
        private val activeTypeRepository: ActiveTypeRepository
) {
    @Transactional(readOnly = true)
    fun list(): MutableList<ActiveTypeDTO>? {
        val list = activeTypeRepository.findAll()
        if(null!=list)
        {
            var listDto = arrayListOf<ActiveTypeDTO>()
            for(l in list)
            {
                val dto = ActiveTypeDTO()
                BeanUtils.copyProperties(l,dto)
                listDto.add(dto)
            }
            return listDto
        }
        return null
    }
}