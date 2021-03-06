package com.skywalker.active.service

import com.skywalker.active.dto.ActiveDTO
import com.skywalker.active.form.ActiveForm
import com.skywalker.active.repository.ActiveImgRepository
import com.skywalker.active.repository.ActiveLeaveMessageRepository
import com.skywalker.active.repository.ActiveRepository
import com.skywalker.active.repository.ActiveUserRepository
import com.skywalker.active.repository.impl.ActiveRepositoryImpl
import com.skywalker.active.web.ActiveController
import com.skywalker.base.bo.MhoSkywalkerActive
import com.skywalker.base.bo.MhoSkywalkerActiveImage
import com.skywalker.base.bo.MhoSkywalkerActiveLeaveMessage
import com.skywalker.base.bo.MhoSkywalkerActiveUser
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.sun.jmx.snmp.EnumRowStatus.active
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import java.text.SimpleDateFormat
import java.util.*


@Service
@Transactional(readOnly = true)
class ActiveService(
    private val activeRepository: ActiveRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val activeImgRepository: ActiveImgRepository,
    private val activeLeaveMessageRepository: ActiveLeaveMessageRepository,
    private val activeRepositoryImpl: ActiveRepositoryImpl
) {

    @Value("\${app.active.create.times}")
    private val activeCteateTimes: Long = 0L

    /**
     * 加入活动
     */
    @Transactional
    fun create(activeId: Long, userId: Long): String {

        val bo = activeUserRepository.findByActiveIdAndUserId(activeId, userId)
        if (null != bo) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1111, ErrorConstants.ERROR_MSG_1111)
        }
        try {
            var user = MhoSkywalkerActiveUser()
            user.activeId = activeId!!
            user.userId = userId
            user.timeCreate = Date()
            activeUserRepository.save(user)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
        }
        return "成功"
    }

    /**
     * 留言
     */
    @Transactional
    fun createMsg(activeId: Long, userId: Long, parentLeaveMessageId: Long?, content: String): String {
        try {
            var msg = MhoSkywalkerActiveLeaveMessage()
            msg.activeId = activeId
            msg.parentLeaveMessageId = parentLeaveMessageId
            msg.content = content
            msg.userId = userId
            msg.timeCreate = Date()
            activeLeaveMessageRepository.save(msg)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
        }
        return "成功"
    }

    /**
     * 活动列表
     */
    fun listAllByTypeId(typeId: Long?, time: Long, pageable: Pageable): HashMap<String, Any?>? {
        if (null == typeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            val date = Date(time)
            val page = activeRepository.listAllByTypeId(typeId, date, pageable)
            if (null != page && !CollectionUtils.isEmpty(page.content)) {
                for (active in page.content) {
                    active.listActiveUserDTO = activeUserRepository.listAllByActiveId(active.activeId)
                    active.listActiveImgDTO = activeImgRepository.listAllByActiveId(active.activeId)
                }
            }
            return hashMapOf("total" to (page?.totalElements ?: 0), "list" to page?.content)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    /**
     * 最新活动列表
     */
    fun listAllNewByTypeId(typeId: Long, time: Long): HashMap<String, Any?> {
        if (null == time) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        val date = Date(time)
        try {
            val list = activeRepository.findByTimeCreateAfter(typeId, date)
            if (!CollectionUtils.isEmpty(list)) {
                for (active in list!!) {
                    active.listActiveUserDTO = activeUserRepository.listAllByActiveId(active.activeId)
                    active.listActiveImgDTO = activeImgRepository.listAllByActiveId(active.activeId)
                }
            }
            return hashMapOf("total" to (list?.size ?: 0), "list" to list)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    /**
     * 活动详情
     */
    fun findByActiveId(activeId: Long?): ActiveDTO? {
        if (null == activeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            val activeDTO = activeRepository.listAllByActiveId(activeId)
            if (null != activeDTO) {
                activeDTO.listActiveUserDTO = activeUserRepository.listAllByActiveId(activeId)
                activeDTO.listActiveImgDTO = activeImgRepository.listAllByActiveId(activeId)
            }
            return activeDTO
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    /**
     * 留言列表
     */
    fun listActiveMsgByActiveId(
        activeId: Long?,
        time: Long,
        pageable: Pageable?
    ): HashMap<String, Any?> {
        if (null == activeId) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            val date = Date(time)
            if (null == pageable) {
                val list = activeLeaveMessageRepository.findByTimeCreateAfter(activeId, date)
                return hashMapOf("total" to (list?.size ?: 0), "list" to list)
            }
            val page = activeLeaveMessageRepository.listAllParentByActiveId(activeId, date, pageable)
            return hashMapOf("total" to (page?.totalElements ?: 0), "list" to page?.content)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

    /**
     * 添加活动
     */
    @Transactional
    fun create(activeForm: ActiveForm): Long {

        //当前时间减去1天
        var preDate = Date(Date().time - 24 * 60 * 60 * 1000)
        var list = activeRepository.listActiveByUserId(activeForm.postUserId, preDate)
        if (!CollectionUtils.isEmpty(list) && list!!.size >= activeCteateTimes) {
            throw ServiceException(
                ErrorConstants.ERROR_CODE_1112,
                ErrorConstants.ERROR_MSG_1112 + activeCteateTimes + "次"
            )
        }

        try {
            var active = MhoSkywalkerActive()
            BeanUtils.copyProperties(activeForm, active)
            active.timeCreate = Date()
            active.goTime = SimpleDateFormat("yyyy-MM-dd").parse(activeForm.goTimeStr)
            activeRepository.save(active)
            return active.activeId
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1113, ErrorConstants.ERROR_MSG_1113, e)
        }
    }

    /**
     * 添加活动图片
     */
    @Transactional
    fun createActiveImg(activeId: Long, imageName: String, imageUrl: String) {
        try {
            var img = MhoSkywalkerActiveImage()
            img.activeId = activeId
            img.imageName = imageName
            img.imageUrl = imageUrl
            activeImgRepository.save(img)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1113, ErrorConstants.ERROR_MSG_1113, e)
        }
    }

    /**
     * 活动列表
     */
    fun listAllByParam(params: ActiveController.ActiveFormParams?, pageable: Pageable?): HashMap<String, Any?> {
        if (null == params) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1107, ErrorConstants.ERROR_MSG_1107)
        }
        try {
            val map = activeRepositoryImpl.listAllByParam(params, pageable)
            if (null != map && null != map["list"]) {
                for (active in map["list"] as List<ActiveDTO>) {
                    active.listActiveUserDTO = activeUserRepository.listAllByActiveId(active.activeId)
                    active.listActiveImgDTO = activeImgRepository.listAllByActiveId(active.activeId)
                }
            }
            return map
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }

/*    */
    /**
     * 活动列表
     *//*
    @Transactional(readOnly = true)
    fun listAllByParam(params: ActiveDTO?, pageable: Pageable): Page<ActiveDTO>? {
        try {
            class mySpec : Specification<MhoSkywalkerActive> {
                override fun toPredicate(root: Root<MhoSkywalkerActive>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate {

                    val startAddressName: Path<String> = root["startAddressName"]
                    //val p1 = criteriaBuilder.like(startAddressName,"%params.startAddressName%")
                    val list = mutableListOf<Predicate>()
                    with(criteriaBuilder) {
                        params?.startAddressName.let { list.add(like(startAddressName, "%$params.startAddressName%")) }
                    }
                    return criteriaBuilder.and(*list.toTypedArray()) //这里将list先装换为array，然后再展开，涉及到list的varags
                }
            }

            val list = activeRepository.findAll(mySpec(),pageable)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1110, ErrorConstants.ERROR_MSG_1110, e)
        }
    }*/
    /**
     * 活动详情
     */
    fun findByActiveIdOnly(activeId: Long): ActiveDTO {
        try {
            return activeRepository.listAllByActiveId(activeId)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_MSG_1110, e)
        }
    }
}