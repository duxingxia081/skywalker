package    com.skywalker.travelnotes.repository

import com.skywalker.base.bo.MhoSkywalkerTravelNotesImage
import com.skywalker.travelnotes.dto.TravelNotesImgDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TravelNotesImgRepository: JpaRepository<MhoSkywalkerTravelNotesImage, Long> {
    @Query("select new com.skywalker.travelnotes.dto.TravelNotesImgDTO(i.imageName,i.imageUrl) from MhoSkywalkerTravelNotesImage i where i.travelNotesId=?1")
    fun listAllByTravelNotesId(travelNotesId: Long): List<TravelNotesImgDTO>?
}