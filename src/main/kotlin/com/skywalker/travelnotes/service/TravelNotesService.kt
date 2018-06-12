package com.skywalker.active.service

import com.skywalker.base.bo.MhoSkywalkerTravelNotes
import com.skywalker.base.bo.MhoSkywalkerTravelNotesImage
import com.skywalker.base.bo.MhoSkywalkerTravelNotesLike
import com.skywalker.base.bo.MhoSkywalkerTravelNotesMessage
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.utils.BaseTools
import com.skywalker.travelnotes.dto.TravelNotesDTO
import com.skywalker.travelnotes.dto.TravelNotesParamDTO
import com.skywalker.travelnotes.repository.TravelNotesImgRepository
import com.skywalker.travelnotes.repository.TravelNotesLikeRepository
import com.skywalker.travelnotes.repository.TravelNotesMessageRepository
import com.skywalker.travelnotes.repository.TravelNotesRepository
import com.skywalker.travelnotes.form.TravelNotesForm
import com.skywalker.travelnotes.form.TravelNotesMessageForm
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
    private val travelNotesImgRepository: TravelNotesImgRepository,
    private val baseTools: BaseTools
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

    /**
     * 点赞
     */
    @Transactional
    fun create(travelNotesId: Long, userId: Long): Long {

        val like = travelNotesLikeRepository.findByParam(travelNotesId, userId)
        if (null != like) {
            throw ServiceException("一条游记只能点赞一次")
        }
        try {
            var bo = MhoSkywalkerTravelNotesLike()
            bo.travelNotesId = travelNotesId
            bo.dolikeUserId = userId
            travelNotesLikeRepository.save(bo)
            return bo.likeId
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1113, ErrorConstants.ERROR_MSG_1113, e)
        }
    }

    /**
     * 取消点赞
     */
    @Transactional
    fun delete(travelNotesId: Long, userId: Long) {
        try {
            val like = travelNotesLikeRepository.findByParam(travelNotesId, userId)
            if (null != like) {
                travelNotesLikeRepository.delete(like)
            }
        } catch (e: Exception) {
            throw ServiceException("取消点赞失败", e)
        }
    }

    /**
     * 留言列表
     */
    fun listTravelNotesMsgByTravelNotesId(
        travelNotesId: Long?,
        pageable: Pageable
    ): Page<MhoSkywalkerTravelNotesMessage>? {
        if (null == travelNotesId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            return travelNotesMessageRepository.listAllParentByTravelNotesId(travelNotesId, pageable)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    /**
     * 留言
     */
    @Transactional
    fun createMsg(form: TravelNotesMessageForm): String {
        try {
            var msg = MhoSkywalkerTravelNotesMessage()
            BeanUtils.copyProperties(form, msg)
            travelNotesMessageRepository.save(msg)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
        }
        return "成功"
    }

    /**
     * 活动详情
     */
    fun getUserNameByTravelNotesId(travelNotesId: Long): String? {
        try {
            val travelNotes = travelNotesRepository.getOne(travelNotesId)
            return baseTools.getUserNameByUserId(travelNotes.postUserId)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_MSG_1110, e)
        }
    }
}