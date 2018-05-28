package com.skywalker.active.repository

import com.skywalker.base.bo.MhoSkywalkerActive
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ActiveRepository : JpaRepository<MhoSkywalkerActive, Long>
{
    @Query("from MhoSkywalkerActive f where f.postUserId=?1")
    fun listAllByTypeId(typeId: Long, pageable: Pageable): Page<MhoSkywalkerActive>
}