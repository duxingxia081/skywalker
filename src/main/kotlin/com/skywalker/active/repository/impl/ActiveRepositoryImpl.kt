package com.skywalker.active.repository.impl

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.active.web.ActiveController
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ActiveRepositoryImpl(
    @PersistenceContext
    private val em: EntityManager
) {
    fun listAllByParam(params: ActiveController.ActiveFormParams, pageable: Pageable?): HashMap<String, Any?> {
        val startAddressName = params.startAddressName
        val endAddressName = params.endAddressName
        val goTime = params.goTime
        val date = params.date
        val dateAfter = params.dateAfter
        var sql =
            "select new com.skywalker.active.dto.ActiveDTO(f.activeId,f.activeTitle,f.postUserId,f.typeId,f.startAddressName,f.startAddressCoordinate,f.endAddressName,f.endAddressCoordinate,f.goTime,f.days,f.charge,f.content,f.coverImage,u.userName,u.nickname,u.headImage,t.typeName,f.timeCreate)"
        var sqlCount = "select count(f.activeId)"
        var hql =
            " from MhoSkywalkerActive f,MhoSkywalkerUser u,MhoSkywalkerActiveType t where f.postUserId=u.userId and f.typeId=t.typeId"
        if (null != startAddressName) {
            hql += " and f.startAddressName like '%$startAddressName'"
        }
        if (null != endAddressName) {
            hql += " and f.endAddressName ='$endAddressName'"
        }
        if (null != goTime) {
            hql += " and date_format(f.goTime,'%Y-%m-%d') = '$goTime'"
        }
        if (null != date) {
            hql += " and f.timeCreate <:date"
        }
        if (null != dateAfter) {
            hql += " and f.timeCreate >:dateAfter"
        }
        hql += " order by f.timeCreate desc"
        val query = em.createQuery(sql + hql)
        val queryCount = em.createQuery(sqlCount + hql)
        if (null != date) {
            query.setParameter("date", Date(date))
            queryCount.setParameter("date", Date(date))
        }
        if (null != dateAfter) {
            query.setParameter("dateAfter", Date(dateAfter))
            queryCount.setParameter("dateAfter", Date(dateAfter))
        }
        if(null!=pageable)
        {
            query.firstResult = pageable.pageNumber * pageable.pageSize
            query.maxResults = pageable.pageSize
        }
        val list = query.resultList
        val count = queryCount.resultList.first()

        em.close()
        return hashMapOf("total" to count, "list" to list as List<ActiveDTO>)
    }
}