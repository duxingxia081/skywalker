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
     * 活动类型活动列表
     */
    @GetMapping("/{typeId}/activities")
    fun list(
        @RequestParam(value = "size", required = false) size: Int?,
        @RequestParam(value = "time") time: Long,
        @PathVariable typeId: Long
    ): SuccessResponse {
        var map: HashMap<String, Any?>?
        map = if (null != size) {
            val pageable = PageRequest(0, size)
            activeService.listAllByTypeId(typeId, time, pageable)
        } else {
            activeService.listAllNewByTypeId(typeId, time)
        }
        return SuccessResponse(map)
    }
}