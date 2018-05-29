package    com.skywalker.active.repository

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.base.bo.MhoSkywalkerActive
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ActiveRepository : JpaRepository<MhoSkywalkerActive, Long> {
    @Query("select f.activeId,f.activeTitle,f.postUserId,f.startAddressName,f.startAddressCoordinate,f.endAddressName,f.endAddressCoordinate,f.goTime,f.days,f.charge,f.content,f.coverImage from com.skywalker.active.dto.ActiveDTO f where f.postUserId=?1")
    fun listAllByTypeId(typeId: Long, pageable: Pageable): Page<ActiveDTO>
}