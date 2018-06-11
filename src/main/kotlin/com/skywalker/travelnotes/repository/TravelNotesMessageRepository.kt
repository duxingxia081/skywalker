package    com.skywalker.travelnotes.repository

import com.skywalker.base.bo.MhoSkywalkerTravelNotesMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TravelNotesMessageRepository: JpaRepository<MhoSkywalkerTravelNotesMessage, Long> {
    @Query("select count(m.messageId) from MhoSkywalkerTravelNotesMessage m where m.travelNotesId=?1")
    fun countByTravelNotesId(travelNotesId: Long): Long
}