package com.skywalker.active.web

import com.skywalker.active.dto.ActiveTypeDTO
import com.skywalker.active.service.ActiveService
import com.skywalker.active.service.ActiveTypeService
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.SuccessResponse
import org.hibernate.validator.constraints.Length
import org.springframework.data.domain.PageRequest
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank


@RestController
class ActiveController(private val activeTypeService: ActiveTypeService, private val activeService: ActiveService, private val jwtTokenUtil: JwtTokenUtil) {
    /**
     * 活动类型
     */
    @RequestMapping(value = "/activeType", method = arrayOf(RequestMethod.GET))
    fun activeType(): MutableList<ActiveTypeDTO>? {
        return activeTypeService.list()
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
    @GetMapping("/activity/{activity}")
    fun list(
            @PathVariable activity: Long?
    ): SuccessResponse {
        val activeDTO = activeService.listAllByActiveId(activity)
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
    fun createActivityUser(@PathVariable activeId: Long, request: HttpServletRequest
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
    fun createMsg(@Valid @RequestBody params: AuthParams,
                  @PathVariable activeId: Long,
                  request: HttpServletRequest,
                  result: BindingResult
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

    data class AuthParams(
            val parentLeaveMessageId: Long?,
            @field:NotBlank(message = "留言内容不能为空")
            @field:Length(max = 100, message = "最大长度不能超过100个文字")
            val content: String
    )
}