package com.skywalker.active.repository

import com.skywalker.active.dto.ActiveLeaveMessageDTO
import com.skywalker.base.bo.MhoSkywalkerActiveLeaveMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ActiveLeaveMessageRepository : JpaRepository<MhoSkywalkerActiveLeaveMessage, Long> {
    @Query("select new com.skywalker.active.dto.ActiveLeaveMessageDTO(u.userName,u.nickname,u.headImage,m.content,m.timeCreate) from MhoSkywalkerActiveLeaveMessage m,MhoSkywalkerUser u where m.userId=u.userId and m.activeId=?1")
    fun listAllByActiveId(activeId: Long, pageable: Pageable): Page<ActiveLeaveMessageDTO>

    @Query("select m from MhoSkywalkerActiveLeaveMessage m,MhoSkywalkerUser u where m.userId=u.userId and m.activeId=?1 and m.parentLeaveMessageId=null")
    fun listAllParentByActiveId(activeId: Long, pageable: Pageable): Page<MhoSkywalkerActiveLeaveMessage>
}