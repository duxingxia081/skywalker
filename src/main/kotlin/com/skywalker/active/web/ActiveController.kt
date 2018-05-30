package com.skywalker.active.web

import com.skywalker.active.dto.ActiveTypeDTO
import com.skywalker.active.service.ActiveService
import com.skywalker.active.service.ActiveTypeService
import com.skywalker.core.response.SuccessResponse
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*


@RestController
class ActiveController(private val activeTypeService: ActiveTypeService, private val activeService: ActiveService) {
    @RequestMapping(value = "/activeType", method = arrayOf(RequestMethod.GET))
    fun activeType(): MutableList<ActiveTypeDTO>? {
        return activeTypeService.list()
    }

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

    @GetMapping("/activity/{activity}")
    fun list(
            @PathVariable activity: Long?
    ): SuccessResponse {
        val activeDTO = activeService.listAllByActiveId(activity)
        return SuccessResponse(activeDTO)
    }

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
}