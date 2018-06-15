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
import com.skywalker.travelnotes.form.TravelNotesForm
import com.skywalker.travelnotes.form.TravelNotesMessageForm
import com.skywalker.travelnotes.repository.TravelNotesImgRepository
import com.skywalker.travelnotes.repository.TravelNotesLikeRepository
import com.skywalker.travelnotes.repository.TravelNotesMessageRepository
import com.skywalker.travelnotes.repository.TravelNotesRepository
import com.skywalker.travelnotes.repository.impl.TravelNotesRepositoryImpl
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@Transactional(readOnly = true)
class TravelNotesService(
    private val travelNotesRepository: TravelNotesRepository,
    private val travelNotesMessageRepository: TravelNotesMessageRepository,
    private val travelNotesLikeRepository: TravelNotesLikeRepository,
    private val travelNotesImgRepository: TravelNotesImgRepository,
    private val travelNotesRepositoryImpl: TravelNotesRepositoryImpl,
    private val baseTools: BaseTools
) {
    /**
     * 游记列表
     */
    fun listAll(param: TravelNotesParamDTO, pageable: Pageable?): HashMap<String, Any?>? {
        try {
            var list = travelNotesRepositoryImpl.listAllByParam(param, pageable)
            if (null != list && null != list["list"]) {
                for (travelNote in list["list"] as List<TravelNotesDTO>) {
                    travelNote.notesLikeCount =
                            travelNotesLikeRepository.countByTravelNotesId(travelNote.travelNotesId)
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
        time: Long,
        pageable: Pageable?
    ): HashMap<String, Any?>? {
        if (null == travelNotesId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        return try {
            val date = Date(time)
            if (null != pageable) {
                val page = travelNotesMessageRepository.listAllParentByTravelNotesId(travelNotesId, date, pageable)
                hashMapOf("total" to (page?.totalElements ?: 0), "list" to page?.content)
            } else {
                val list = travelNotesMessageRepository.findByTimeCreateAfter(travelNotesId, date)
                hashMapOf("total" to (list?.size ?: 0), "list" to list)
            }
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