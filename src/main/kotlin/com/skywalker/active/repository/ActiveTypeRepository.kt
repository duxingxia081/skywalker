package com.skywalker.active.repository

import com.skywalker.base.bo.MhoSkywalkerActiveType
import org.springframework.data.jpa.repository.JpaRepository

interface ActiveTypeRepository : JpaRepository<MhoSkywalkerActiveType, Long>