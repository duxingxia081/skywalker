package com.skywalker.travelnotes.repository.impl

import com.skywalker.travelnotes.dto.TravelNotesDTO
import com.skywalker.travelnotes.dto.TravelNotesParamDTO
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class TravelNotesRepositoryImpl(
    @PersistenceContext
    private val em: EntityManager
) {
    fun listAllByParam(param: TravelNotesParamDTO, pageable: Pageable?): HashMap<String, Any?> {
        val postUserId = param.postUserId
        val travelNotesId = param.travelNotesId
        val date = param.date
        val dateAfter = param.dateAfter
        var sql =
            "select new com.skywalker.travelnotes.dto.TravelNotesDTO(t.travelNotesId,t.title,u.userName,u.nickname,u.headImage,t.addressName,t.addressCoordinate,t.content,t.timeCreate)"
        var sqlCount = "select count(t.travelNotesId)"
        var hql =
            "  from MhoSkywalkerTravelNotes t,MhoSkywalkerUser u where t.postUserId=u.userId"
        if (null != postUserId) {
            hql += " and u.userId ='$postUserId'"
        }
        if (null != travelNotesId) {
            hql += " and t.travelNotesId='$travelNotesId'"
        }
        if (null != date) {
            hql += " and t.timeCreate <:date"
        }
        if (null != dateAfter) {
            hql += " and t.timeCreate >:dateAfter"
        }
        hql += " order by t.timeCreate desc"
        val query = em.createQuery(sql + hql)
        val queryCount = em.createQuery(sqlCount + hql)
        if (null != date) {
            query.setParameter("date", date)
            queryCount.setParameter("date", date)
        }
        if (null != dateAfter) {
            query.setParameter("dateAfter", dateAfter)
            queryCount.setParameter("dateAfter", dateAfter)
        }
        if (null != pageable) {
            query.firstResult = pageable.pageNumber * pageable.pageSize
            query.maxResults = pageable.pageSize
        }
        val list = query.resultList
        val count = queryCount.resultList.first()

        em.close()
        return hashMapOf("total" to count, "list" to list as List<TravelNotesDTO>)
    }
}