package    com.skywalker.active.repository

import com.skywalker.active.dto.ActiveUserDTO
import com.skywalker.base.bo.MhoSkywalkerActiveUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ActiveUserRepository : JpaRepository<MhoSkywalkerActiveUser, Long> {
    @Query("select new com.skywalker.active.dto.ActiveUserDTO(u.userId,u.nickname,u.headImage) from MhoSkywalkerUser u,MhoSkywalkerActiveUser au where u.userId=au.userId and au.activeId=?1")
    fun listAllByActiveId(activeId: Long): List<ActiveUserDTO>?
    @Query("from MhoSkywalkerActiveUser au where au.userId=?2 and au.activeId=?1")
    fun findByActiveIdAndUserId(activeId: Long,userId:Long): MhoSkywalkerActiveUser?
}