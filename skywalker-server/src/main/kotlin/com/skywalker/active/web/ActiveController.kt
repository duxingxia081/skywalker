package com.skywalker.active.web

import com.skywalker.active.form.ActiveForm
import com.skywalker.active.service.ActiveService
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.ServerMessage
import com.skywalker.core.response.SuccessResponse
import com.skywalker.core.utils.BaseTools
import com.skywalker.core.utils.BaseUtils
import org.hibernate.validator.constraints.Length
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.util.CollectionUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank


@RestController
@RequestMapping("/activity")
@CrossOrigin
class ActiveController(
        private val activeService: ActiveService,
        private val jwtTokenUtil: JwtTokenUtil,
        private val baseTools: BaseTools
) {
    @Value("\${app.img.activity}")
    private val activeImgPath: String = ""
    @Value("\${app.img.type}")
    private val suffixList: String = ""

    /**
     * 新增活动
     */
    @PostMapping
    fun createActive(
            @Valid @RequestBody params: ActiveForm,
            result: BindingResult,
            request: HttpServletRequest
    ): SuccessResponse {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.fieldErrors)
        }
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        params.postUserId = userId
        val activeId = activeService.create(params)
        fileUpload(params.file, activeId)
        return SuccessResponse("成功")
    }

    /**
     * 活动详情
     */
    @GetMapping("/{activeId}")
    fun list(
            @PathVariable activeId: Long?
    ): SuccessResponse {
        val activeDTO = activeService.findByActiveId(activeId)
        return SuccessResponse(activeDTO)
    }

    /**
     * 加入活动
     */
    @PostMapping("/{activeId}/activityUser")
    fun createActivityUser(
            @PathVariable activeId: Long, request: HttpServletRequest
    ): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        val result = activeService.create(activeId, userId)
        val dto = activeService.findByActiveIdOnly(activeId)
        baseTools.convertAndSendToUser(
                dto.userName!!,
                ServerMessage("加入活动", "有用户加入到你发布的活动")
        )
        return SuccessResponse(result)
    }

    private fun fileUpload(list: List<MultipartFile>?, activeId: Long) {
        if (!CollectionUtils.isEmpty(list)) {
            try {
                for (file in list!!) {
                    val name = BaseUtils.fileUpLoad(file, activeImgPath, suffixList)
                    activeService.createActiveImg(activeId, name, "img/activeImg/$name")
                }
            } catch (e: IOException) {
                throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
            }

        }
    }

    /**
     * 活动列表
     */
    @GetMapping
    fun listActivity(
            @RequestParam(value = "size", required = false) size: Int?,
            @RequestParam(value = "startAddressName") startAddressName: String?,
            @RequestParam(value = "endAddressName") endAddressName: String?,
            @RequestParam(value = "activeCategory") activeCategory: String?,
            @RequestParam(value = "goTime") goTime: String?,
            @RequestParam(value = "activeId") activeId: Long?,
            @RequestParam(value = "userId") userId: Long?
    ): SuccessResponse {
        var params = ActiveFormParams(startAddressName, endAddressName, goTime, activeCategory, activeId, userId)
        var map: HashMap<String, Any?>?
        var pageable: PageRequest = if (null != size) {
            PageRequest(0, size)
        } else {
            PageRequest(0, 5)
        }
        map = activeService.listAllByParam(params, pageable)
        return SuccessResponse(map)

    }

    data class ActiveFormParams(
            var startAddressName: String? = null,
            var endAddressName: String? = null,
            var goTime: String? = null,
            var activeCategory: String? = null,
            var activeId: Long? = null,
            var userId: Long? = null
    )

    /**
     * 活动留言
     */
    @PostMapping("/{activeId}/activityMsg")
    fun createMsg(
            @Valid @RequestBody params: MsgParams,
            result: BindingResult,
            @PathVariable activeId: Long,
            request: HttpServletRequest
    ): SuccessResponse {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.fieldErrors)
        }
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        val result = activeService.createMsg(activeId, userId, params.parentLeaveMessageId, params.content)
        val dto = activeService.findByActiveIdOnly(activeId)
        baseTools.convertAndSendToUser(
                dto.userName!!,
                ServerMessage("活动留言", "你发布的活动有用户留言，请注意查看")
        )
        return SuccessResponse(result)
    }

    data class MsgParams(
            val parentLeaveMessageId: Long? = null,
            @field:NotBlank(message = "留言内容不能为空")
            @field:Length(max = 100, message = "最大长度不能超过100个文字")
            val content: String = ""
    )

    /**
     * 留言列表
     */
    @GetMapping("/{activity}/activityLeaveMsg")
    fun listMsg(
            @RequestParam(value = "size", required = false) size: Int?,
            @RequestParam(value = "time") time: Long,
            @PathVariable activity: Long?
    ): SuccessResponse {
        var map: HashMap<String, Any?>?
        map = if (null != size) {
            val pageable = PageRequest(0, size)
            activeService.listActiveMsgByActiveId(activity, time, pageable)
        } else {
            activeService.listActiveMsgByActiveId(activity, time, null)
        }
        return SuccessResponse(map)
    }
}