package com.skywalker.travelnotes.web

import com.skywalker.active.service.TravelNotesService
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.ServerMessage
import com.skywalker.core.response.SuccessResponse
import com.skywalker.core.utils.BaseUtils
import com.skywalker.travelnotes.dto.TravelNotesParamDTO
import com.skywalker.travelnotes.form.TravelNotesForm
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.util.CollectionUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
class TravelNotesController(
    private val travelNotesService: TravelNotesService,
    private val jwtTokenUtil: JwtTokenUtil
) {
    @Value("\${app.img.travelNote}")
    private val travelNotesImgPath: String = ""
    @Value("\${app.img.type}")
    private val suffixList: String = ""

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

    /**
     * 新增游记
     */
    @PostMapping("/travelNotes")
    fun createTravelNotes(
        @Valid params: TravelNotesForm,
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
        val travelNotesId = travelNotesService.create(params)
        fileUpload(params.file, travelNotesId)
        return SuccessResponse("成功")
    }

    private fun fileUpload(list: List<MultipartFile>?, travelNotesId: Long) {
        if (!CollectionUtils.isEmpty(list)) {
            try {
                for (file in list!!) {
                    val name = BaseUtils.fileUpLoad(file, travelNotesImgPath, suffixList)
                    travelNotesService.createTravelNotesImg(travelNotesId, name, "img/travelNote/$name")
                }
            } catch (e: IOException) {
                throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
            }

        }
    }

    /**
     * 点赞
     */
    @PostMapping("/travelNotes/{travelNotesId}/travelNotesLike")
    fun createTravelNotesLike(
        @PathVariable travelNotesId: Long, request: HttpServletRequest
    ): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        val result = travelNotesService.create(travelNotesId, userId)
        return SuccessResponse(result)
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/travelNotes/{travelNotesId}/travelNotesLike")
    fun delTravelNotesLike(
        @PathVariable travelNotesId: Long, request: HttpServletRequest
    ): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        travelNotesService.delete(travelNotesId, userId)
        return SuccessResponse("取消点赞成功")
    }
}