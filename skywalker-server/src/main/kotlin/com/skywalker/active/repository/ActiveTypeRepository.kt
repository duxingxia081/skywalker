package com.skywalker.active.repository

import com.skywalker.base.bo.MhoSkywalkerActiveType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActiveTypeRepository : JpaRepository<MhoSkywalkerActiveType, Long>