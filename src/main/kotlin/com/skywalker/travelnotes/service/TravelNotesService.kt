package com.skywalker.active.service

import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.travelnotes.dto.TravelNotesDTO
import com.skywalker.travelnotes.repository.TravelNotesLikeRepository
import com.skywalker.travelnotes.repository.TravelNotesMessageRepository
import com.skywalker.travelnotes.repository.TravelNotesRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils


@Service
@Transactional(readOnly = true)
class TravelNotesService(
    private val travelNotesRepository: TravelNotesRepository,
    private val travelNotesMessageRepository: TravelNotesMessageRepository,
    private val travelNotesLikeRepository: TravelNotesLikeRepository
) {
    /**
     * 活动列表
     */
    fun listAll(pageable: Pageable): Page<TravelNotesDTO>? {
        try {
            val list = travelNotesRepository.listAll(pageable)
            if (null != list && !CollectionUtils.isEmpty(list.content)) {
                for (travelNote in list.content) {
                    travelNote.notesLikeCount = travelNotesLikeRepository.countByTravelNotesId(travelNote.travelNotesId)
                    travelNote.notesMsgCount =
                            travelNotesMessageRepository.countByTravelNotesId(travelNote.travelNotesId)
                }
            }
            return list
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }
}