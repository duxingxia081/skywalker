package com.skywalker.active.repository.impl

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.active.web.ActiveController
import io.micrometer.core.instrument.util.StringUtils
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
        val activeCategory = params.activeCategory
        val activeId = params.activeId
        val userId = params.userId
        val type = params.type
        val activityType = params.activityType
        var sql =
                "select new com.skywalker.active.dto.ActiveDTO(f.activeId,f.activeTitle,f.postUserId,f.typeId,f.startAddressName,f.startAddressCoordinate,f.endAddressName,f.endAddressCoordinate,f.goTime,f.days,f.charge,f.content,f.coverImage,u.userName,u.nickname,u.headImage,t.typeName,f.timeCreate)"
        var sqlCount = "select count(f.activeId)"
        var hql =
                " from MhoSkywalkerActive f,MhoSkywalkerUser u,MhoSkywalkerActiveType t "
        hql += if (StringUtils.isNotBlank(type) && "00" != type) {
            " ,MhoSkywalkerActiveUser au where au.activeId=f.activeId"
        } else {
            " where 1=1"
        }
        hql += " and f.postUserId=u.userId and f.typeId=t.typeId"
        if (null != startAddressName) {
            hql += " and f.startAddressName like '%$startAddressName'"
        }
        if (null != endAddressName) {
            hql += " and f.endAddressName ='$endAddressName'"
        }
        if (null != activeCategory) {
            hql += " and f.activeCategory ='$activeCategory'"
        }
        if (null != userId) {
            hql += if (StringUtils.isNotBlank(type) && "00" != type) {
                " and au.userId = $userId"
            } else {
                " and f.postUserId = $userId"
            }
        }
        if (null != goTime) {
            hql += " and date_format(f.goTime,'%Y-%m-%d') = '$goTime'"
        }
        if (null != activityType) {
            if (activityType == 0L) {
                hql += " and f.activeCategory='0'"
            } else {
                hql += " and f.typeId=$activityType"
            }
        }
        if (null != activeId) {
            hql += if (null != pageable) {
                " and f.activeId <:activeId"
            } else {
                " and f.activeId >:activeId"
            }
        }
        hql += " order by f.activeId desc"
        val query = em.createQuery(sql + hql)
        val queryCount = em.createQuery(sqlCount + hql)
        if (null != activeId) {
            query.setParameter("activeId", activeId)
            queryCount.setParameter("activeId", activeId)
        }
        if (null != pageable) {
            query.firstResult = pageable.pageNumber * pageable.pageSize
            query.maxResults = pageable.pageSize
        }
        val list = query.resultList
        val count = queryCount.resultList.first()

        em.close()
        return hashMapOf("total" to count, "list" to list as List<ActiveDTO>)
    }
}