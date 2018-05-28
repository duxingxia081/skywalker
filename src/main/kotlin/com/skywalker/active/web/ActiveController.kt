package com.skywalker.active.web

import com.skywalker.active.dto.ActiveTypeDTO
import com.skywalker.active.service.ActiveService
import com.skywalker.active.service.ActiveTypeService
import com.skywalker.base.bo.ActiveDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/activeType")
class ActiveController(private val activeTypeService: ActiveTypeService, private val activeService: ActiveService) {
    @RequestMapping(value = "/activeType", method = arrayOf(RequestMethod.GET))
    fun activeType(): MutableList<ActiveTypeDTO>? {
        return activeTypeService.list()
    }

    @GetMapping("/{activeType}/activities")
    fun list(
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "size", required = false) size: Int?,
            @PathVariable typeId: Int?
    ): Page<ActiveDTO>? {
        val pageable = PageRequest(page ?: 0, size ?: 5)
        return activeService.listAllByTypeId(2L, pageable)
    }
}