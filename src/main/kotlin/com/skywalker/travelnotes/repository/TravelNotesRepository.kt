package com.skywalker.travelnotes.repository

import com.skywalker.base.bo.MhoSkywalkerTravelNotes
import com.skywalker.travelnotes.dto.TravelNotesDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TravelNotesRepository : JpaRepository<MhoSkywalkerTravelNotes, Long> {
    @Query("select new com.skywalker.travelnotes.dto.TravelNotesDTO(t.travelNotesId,t.title,u.userName,u.nickname,u.headImage,t.addressName,t.addressCoordinate,t.content,t.timeCreate) from MhoSkywalkerTravelNotes t,MhoSkywalkerUser u where t.postUserId=u.userId")
    fun listAll(pageable: Pageable): Page<TravelNotesDTO>?
    @Query("select new com.skywalker.travelnotes.dto.TravelNotesDTO(t.travelNotesId,t.title,u.userName,u.nickname,u.headImage,t.addressName,t.addressCoordinate,t.content,t.timeCreate) from MhoSkywalkerTravelNotes t,MhoSkywalkerUser u where t.postUserId=u.userId and u.userId=?1")
    fun listByUserId(userId:Long,pageable: Pageable): Page<TravelNotesDTO>?
    @Query("select new com.skywalker.travelnotes.dto.TravelNotesDTO(t.travelNotesId,t.title,u.userName,u.nickname,u.headImage,t.addressName,t.addressCoordinate,t.content,t.timeCreate) from MhoSkywalkerTravelNotes t,MhoSkywalkerUser u where t.postUserId=u.userId and t.travelNotesId=?1")
    fun listByTravelNotesId(travelNotesId:Long,pageable: Pageable): Page<TravelNotesDTO>?
}