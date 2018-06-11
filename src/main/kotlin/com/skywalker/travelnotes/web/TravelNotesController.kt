package com.skywalker.travelnotes.web

import com.skywalker.active.service.TravelNotesService
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.SuccessResponse
import com.skywalker.travelnotes.dto.TravelNotesParamDTO
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest


@RestController
class TravelNotesController(
    private val travelNotesService: TravelNotesService,
    private val jwtTokenUtil: JwtTokenUtil
) {

    /**
     * 游记列表
     */
    @GetMapping("/travelNotes")
    fun list(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?
    ): SuccessResponse {
        val sort = Sort(Sort.Direction.DESC, "timeCreate")
        val pageable = PageRequest(page ?: 0, size ?: 5, sort)
        val page = travelNotesService.listAll(null, pageable)
        var map: HashMap<String, Any?> = hashMapOf("total" to page?.totalElements, "list" to page?.content)
        return SuccessResponse(map)
    }

    /**
     * 我的游记列表
     */
    @GetMapping("/travelNotes/myTravelNotes")
    fun myTravelNotes(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?,
        request: HttpServletRequest
    ): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        val param = TravelNotesParamDTO()
        param.postUserId = userId
        val sort = Sort(Sort.Direction.DESC, "timeCreate")
        val pageable = PageRequest(page ?: 0, size ?: 5, sort)
        val page = travelNotesService.listAll(param, pageable)
        var map: HashMap<String, Any?> = hashMapOf("total" to page?.totalElements, "list" to page?.content)
        return SuccessResponse(map)
    }

    /**
     * 我的游记列表
     */
    @GetMapping("/{userId}/travelNotes")
    fun otherTravelNotes(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?,
        @PathVariable userId: Long
    ): SuccessResponse {
        val param = TravelNotesParamDTO()
        param.postUserId = userId
        val sort = Sort(Sort.Direction.DESC, "timeCreate")
        val pageable = PageRequest(page ?: 0, size ?: 5, sort)
        val page = travelNotesService.listAll(param, pageable)
        var map: HashMap<String, Any?> = hashMapOf("total" to page?.totalElements, "list" to page?.content)
        return SuccessResponse(map)
    }

    /**
     * 我的游记列表
     */
    @GetMapping("/travelNotes/{travelNotesId}")
    fun travelNotesInfo(
        @RequestParam(value = "page", required = false) page: Int?,
        @RequestParam(value = "size", required = false) size: Int?,
        @PathVariable travelNotesId: Long
    ): SuccessResponse {
        val param = TravelNotesParamDTO()
        param.travelNotesId = travelNotesId
        val pageable = PageRequest(page ?: 0, size ?: 5)
        val page = travelNotesService.listAll(param, pageable)
        return SuccessResponse(page?.content?.get(0))
    }
}