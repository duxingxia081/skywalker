package com.skywalker.active.repository

import com.skywalker.base.bo.MhoSkywalkerActiveLeaveMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActiveLeaveMessageRepository : JpaRepository<MhoSkywalkerActiveLeaveMessage, Long> {
    @Query("select m from MhoSkywalkerActiveLeaveMessage m,MhoSkywalkerUser u where m.userId=u.userId and m.activeId=?1 and m.parentLeaveMessageId=null and m.timeCreate<?2 order by m.timeCreate desc")
    fun listAllParentByActiveId(activeId: Long, date: Date, pageable: Pageable): Page<MhoSkywalkerActiveLeaveMessage>?

    @Query("select m from MhoSkywalkerActiveLeaveMessage m,MhoSkywalkerUser u where m.userId=u.userId and m.activeId=?1 and m.parentLeaveMessageId=null and m.timeCreate>?2 order by m.timeCreate desc")
    fun findByTimeCreateAfter(activeId: Long, date: Date): List<MhoSkywalkerActiveLeaveMessage>?

}