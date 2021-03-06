package    com.skywalker.travelnotes.repository

import com.skywalker.base.bo.MhoSkywalkerTravelNotesLike
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TravelNotesLikeRepository : JpaRepository<MhoSkywalkerTravelNotesLike, Long> {
    @Query("select count(l.likeId) from MhoSkywalkerTravelNotesLike l where l.travelNotesId=?1")
    fun countByTravelNotesId(travelNotesId: Long): Long

    @Query("from MhoSkywalkerTravelNotesLike l where l.travelNotesId=?1 and l.dolikeUserId=?2")
    fun findByParam(travelNotesId: Long, dolikeUserId: Long): MhoSkywalkerTravelNotesLike?

}