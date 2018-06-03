package com.skywalker.active.repository

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.base.bo.MhoSkywalkerActive
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActiveRepository : JpaRepository<MhoSkywalkerActive, Long>, JpaSpecificationExecutor<MhoSkywalkerActive> {

    @Query("select new com.skywalker.active.dto.ActiveDTO(f.activeId,f.activeTitle,f.postUserId,f.typeId,f.startAddressName,f.startAddressCoordinate,f.endAddressName,f.endAddressCoordinate,f.goTime,f.days,f.charge,f.content,f.coverImage,u.userName,u.nickname,u.headImage,t.typeName) from MhoSkywalkerActive f,MhoSkywalkerUser u,MhoSkywalkerActiveType t where f.postUserId=u.userId and f.typeId=t.typeId and f.typeId=?1")
    fun listAllByTypeId(typeId: Long, pageable: Pageable): Page<ActiveDTO>

    @Query("select new com.skywalker.active.dto.ActiveDTO(f.activeId,f.activeTitle,f.postUserId,f.typeId,f.startAddressName,f.startAddressCoordinate,f.endAddressName,f.endAddressCoordinate,f.goTime,f.days,f.charge,f.content,f.coverImage,u.userName,u.nickname,u.headImage,t.typeName) from MhoSkywalkerActive f,MhoSkywalkerUser u,MhoSkywalkerActiveType t where f.postUserId=u.userId and f.typeId=t.typeId and f.activeId=?1")
    fun listAllByActiveId(activeId: Long): ActiveDTO

    @Query("from MhoSkywalkerActive f where f.postUserId=?1 and f.timeCreate>?2")
    fun listActiveByUserId(userId: Long,timeCreate: Date): List<MhoSkywalkerActive>
    @Query("select new com.skywalker.active.dto.ActiveDTO(f.activeId,f.activeTitle,f.postUserId,f.typeId,f.startAddressName,f.startAddressCoordinate,f.endAddressName,f.endAddressCoordinate,f.goTime,f.days,f.charge,f.content,f.coverImage,u.userName,u.nickname,u.headImage,t.typeName) from MhoSkywalkerActive f,MhoSkywalkerUser u,MhoSkywalkerActiveType t where f.postUserId=u.userId and f.typeId=t.typeId")
    fun listAll(typeId: Long, pageable: Pageable): Page<ActiveDTO>
}