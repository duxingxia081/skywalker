package com.skywalker.active.web

import com.skywalker.active.dto.ActiveTypeDTO
import com.skywalker.active.form.ActiveForm
import com.skywalker.active.service.ActiveService
import com.skywalker.active.service.ActiveTypeService
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.SuccessResponse
import com.skywalker.core.utils.BaseTools
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
class ActiveController(
    private val activeTypeService: ActiveTypeService,
    private val activeService: ActiveService,
    private val jwtTokenUtil: JwtTokenUtil
) {
    @Value("\${app.img.activity}")
    private val activeImgPath: String = ""
    @Value("\${app.img.head.type}")
    private val suffixList: String = ""

    /**
     * 活动类型
     */
    @RequestMapping(value = "/activeType", method = arrayOf(RequestMethod.GET))
    fun activeType(): SuccessResponse {
        return return SuccessResponse(activeTypeService.list())
    }

    /**
     * 活动列表
     */
    @GetMapping("/activeType/{typeId}/activities")
    fun list(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?,
        @PathVariable typeId: Long?
    ): SuccessResponse {
        val pageable = PageRequest(page ?: 0, size ?: 5)
        val page = activeService.listAllByTypeId(typeId, pageable)
        var map: HashMap<String, Any?> = hashMapOf("total" to page?.totalElements, "list" to page?.content)
        return SuccessResponse(map)
    }

    /**
     * 活动详情
     */
    @GetMapping("/activity/{activeId}")
    fun list(
        @PathVariable activeId: Long?
    ): SuccessResponse {
        val activeDTO = activeService.findByActiveId(activeId)
        return SuccessResponse(activeDTO)
    }

    /**
     * 留言列表
     */
    @GetMapping("/activity/{activity}/activityLeaveMsg")
    fun listMsg(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?,
        @PathVariable activity: Long?
    ): SuccessResponse {
        val pageable = PageRequest(page ?: 0, size ?: 5)
        val page = activeService.listActiveMsgByActiveId(activity, pageable)
        var map: HashMap<String, Any?> = hashMapOf("total" to page?.totalElements, "list" to page?.content)
        return SuccessResponse(map)
    }

    /**
     * 加入活动
     */
    @PostMapping("/activity/{activeId}/activityUser")
    fun createActivityUser(
        @PathVariable activeId: Long, request: HttpServletRequest
    ): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        return SuccessResponse(activeService.create(activeId, userId))
    }

    /**
     * 活动留言
     */
    @PostMapping("/activity/{activeId}/activityMsg")
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
        return SuccessResponse(activeService.createMsg(activeId, userId, params.parentLeaveMessageId, params.content))
    }

    data class MsgParams(
        val parentLeaveMessageId: Long?,
        @field:NotBlank(message = "留言内容不能为空")
        @field:Length(max = 100, message = "最大长度不能超过100个文字")
        val content: String
    )

    /**
     * 新增活动
     */
    @PostMapping("/activity")
    fun createActive(
        @Valid params: ActiveForm,
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
        fileUpload(params.file,activeId)
        return SuccessResponse("成功")
    }

    private fun fileUpload(list: List<MultipartFile>?,activeId: Long){
        if (!CollectionUtils.isEmpty(list)) {
            try {
                for (file in list!!) {
                    //设置允许上传文件类型
                    BaseTools.checkImgType(file, suffixList)
                    val fileName = activeId.toString() + "." + file.originalFilename!!.substringAfterLast(".")
                    val name = BaseTools.upLoad(file, activeImgPath, fileName)
                    activeService.createActiveImg(activeId, name, "img/heads/$name")
                }
            } catch (e: IOException) {
                throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
            }

        }
    }

    /**
     * 活动列表
     */
    @GetMapping("/activity")
    fun listActivity(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?,
        @RequestParam(value = "startAddressName") startAddressName: String?,
        @RequestParam(value = "endAddressName") endAddressName: String?,
        @RequestParam(value = "goTime") goTime: String?
    ): SuccessResponse {
        val pageable = PageRequest(page ?: 0, size ?: 5)
        var params = ActiveFormParams(startAddressName, endAddressName, goTime)
        val page = activeService.listAllByParam(params, pageable)
        return SuccessResponse(page)

    }

    data class ActiveFormParams(
        var startAddressName: String? = null,
        var endAddressName: String? = null,
        var goTime: String? = null
    )
}