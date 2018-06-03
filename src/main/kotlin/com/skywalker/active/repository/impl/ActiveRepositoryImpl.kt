package com.skywalker.active.repository.impl

import com.skywalker.active.dto.ActiveDTO
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ActiveRepositoryImpl(private @PersistenceContext
                           val em: EntityManager) {
    fun listAllByParam(params: ActiveDTO, pageable: Pageable): List<ActiveDTO> {
        val startAddressName = params.startAddressName
        val endAddressName = params.endAddressName
        var sql = "select new com.skywalker.active.dto.ActiveDTO(f.activeId,f.activeTitle,f.postUserId,f.typeId,f.startAddressName,f.startAddressCoordinate,f.endAddressName,f.endAddressCoordinate,f.goTime,f.days,f.charge,f.content,f.coverImage,u.userName,u.nickname,u.headImage,t.typeName) from MhoSkywalkerActive f,MhoSkywalkerUser u,MhoSkywalkerActiveType t where f.postUserId=u.userId and f.typeId=t.typeId"
        if (null != startAddressName) {
            sql += " and f.startAddressName like '%$startAddressName'"
        }
        if (null != endAddressName) {
            sql += " and f.endAddressName =$endAddressName "
        }
        if (null != params.goTime) {
            sql += " and f.goTime =$params.goTime"
        }
        val query = em.createQuery(sql)
        query.firstResult = pageable.pageNumber * pageable.pageSize
        query.maxResults = pageable.pageSize
        val listRe = query.resultList
        em.close()
        return listRe as List<ActiveDTO>
    }
}