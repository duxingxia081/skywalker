package com.skywalker.active.service

import com.skywalker.base.bo.MhoSkywalkerTravelNotes
import com.skywalker.base.bo.MhoSkywalkerTravelNotesImage
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.travelnotes.dto.TravelNotesDTO
import com.skywalker.travelnotes.dto.TravelNotesParamDTO
import com.skywalker.travelnotes.repository.TravelNotesImgRepository
import com.skywalker.travelnotes.repository.TravelNotesLikeRepository
import com.skywalker.travelnotes.repository.TravelNotesMessageRepository
import com.skywalker.travelnotes.repository.TravelNotesRepository
import com.skywalker.travelnotes.form.TravelNotesForm
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import java.util.*


@Service
@Transactional(readOnly = true)
class TravelNotesService(
    private val travelNotesRepository: TravelNotesRepository,
    private val travelNotesMessageRepository: TravelNotesMessageRepository,
    private val travelNotesLikeRepository: TravelNotesLikeRepository,
    private val travelNotesImgRepository: TravelNotesImgRepository
) {
    /**
     * 游记列表
     */
    fun listAll(param: TravelNotesParamDTO?, pageable: Pageable): Page<TravelNotesDTO>? {
        try {
            var list: Page<TravelNotesDTO>? =
                when {
                    null != param?.postUserId -> travelNotesRepository.listByUserId(param.postUserId!!, pageable)
                    null != param?.travelNotesId -> travelNotesRepository.listByTravelNotesId(
                        param.travelNotesId!!,
                        pageable
                    )
                    else -> travelNotesRepository.listAll(pageable)
                }
            if (null != list && !CollectionUtils.isEmpty(list.content)) {
                for (travelNote in list.content) {
                    travelNote.notesLikeCount = travelNotesLikeRepository.countByTravelNotesId(travelNote.travelNotesId)
                    travelNote.notesMsgCount =
                            travelNotesMessageRepository.countByTravelNotesId(travelNote.travelNotesId)
                    travelNote.listTravelNotesImgDTO =
                            travelNotesImgRepository.listAllByTravelNotesId(travelNote.travelNotesId)
                }
            }
            return list
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    /**
     * 添加游记
     */
    @Transactional
    fun create(travelNotesForm: TravelNotesForm): Long {
        try {
            var bo = MhoSkywalkerTravelNotes()
            BeanUtils.copyProperties(travelNotesForm, bo)
            bo.timeCreate = Date()
            travelNotesRepository.save(bo)
            return bo.travelNotesId
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1113, ErrorConstants.ERROR_MSG_1113, e)
        }
    }

    /**
     * 添加游记图片
     */
    @Transactional
    fun createTravelNotesImg(travelNotesId: Long, imageName: String, imageUrl: String) {
        try {
            var img = MhoSkywalkerTravelNotesImage()
            img.travelNotesId = travelNotesId
            img.imageName = imageName
            img.imageUrl = imageUrl
            travelNotesImgRepository.save(img)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1113, ErrorConstants.ERROR_MSG_1113, e)
        }
    }
}