package com.skywalker.travelnotes.web

import com.skywalker.active.service.TravelNotesService
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.response.SuccessResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class TravelNotesController(
    private val travelNotesService: TravelNotesService,
    private val jwtTokenUtil: JwtTokenUtil
) {

    /**
     * 活动列表
     */
    @GetMapping("/travelNotes")
    fun list(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?
    ): SuccessResponse {
        val sort = Sort(Sort.Direction.DESC, "timeCreate")
        val pageable = PageRequest(page ?: 0, size ?: 5,sort)
        val page = travelNotesService.listAll(pageable)
        var map: HashMap<String, Any?> = hashMapOf("total" to page?.totalElements, "list" to page?.content)
        return SuccessResponse(map)
    }

}