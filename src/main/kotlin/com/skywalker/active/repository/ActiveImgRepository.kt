package    com.skywalker.active.repository

import com.skywalker.active.dto.ActiveImgDTO
import com.skywalker.base.bo.MhoSkywalkerActiveImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ActiveImgRepository : JpaRepository<MhoSkywalkerActiveImage, Long> {
    @Query("select new com.skywalker.active.dto.ActiveImgDTO(u.imageName,u.imageUrl) from MhoSkywalkerActiveImage u,MhoSkywalkerActive a where u.activeId=a.activeId and a.activeId=?1")
    fun listAllByActiveId(activeId: Long): List<ActiveImgDTO>?
}