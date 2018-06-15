package com.skywalker.active.web

import com.skywalker.active.service.ActiveService
import com.skywalker.active.service.ActiveTypeService
import com.skywalker.core.response.SuccessResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/activeType")
class ActiveTypeController(
    private val activeTypeService: ActiveTypeService,
    private val activeService: ActiveService
) {

    /**
     * 活动类型
     */
    @GetMapping
    fun activeType(): SuccessResponse {
        return return SuccessResponse(activeTypeService.list())
    }

    /**
     * 活动类型下滑活动列表
     */
    @GetMapping("/{typeId}/activities")
    fun list(
        @RequestParam(value = "size", required = false) size: Int?,
        @RequestParam(value = "time") time: Long,
        @PathVariable typeId: Long?
    ): SuccessResponse {
        val pageable = PageRequest(0, size ?: 5)
        val page = activeService.listAllByTypeId(typeId, time, pageable)
        var map: HashMap<String, Any?> = hashMapOf("total" to page?.totalElements, "list" to page?.content)
        return SuccessResponse(map)
    }

    /**
     * 活动类型下上滑最新活动列表
     */

    @GetMapping("/{typeId}/activities/newActivity")
    fun listNewActivity(
        @PathVariable typeId: Long,
        @RequestParam(value = "time") time: Long
    ): SuccessResponse {
        return SuccessResponse(activeService.listAllNewByTypeId(typeId, time))
    }
}