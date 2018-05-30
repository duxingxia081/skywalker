package com.skywalker.active.web

import com.skywalker.active.dto.ActiveTypeDTO
import com.skywalker.active.service.ActiveService
import com.skywalker.active.service.ActiveTypeService
import com.skywalker.core.response.ListResponse
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/activeType")
class ActiveController(private val activeTypeService: ActiveTypeService, private val activeService: ActiveService) {
    @RequestMapping(value = "/activeType", method = arrayOf(RequestMethod.GET))
    fun activeType(): MutableList<ActiveTypeDTO>? {
        return activeTypeService.list()
    }

    @GetMapping("/{typeId}/activities")
    fun list(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?,
        @PathVariable typeId: Long?
    ): ListResponse {
        val pageable = PageRequest(page ?: 0, size ?: 5)
        val page = activeService.listAllByTypeId(typeId, pageable)
        return ListResponse(page?.totalElements, page?.content)
    }
}