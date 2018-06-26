package com.skywalker.travelnotes.web

import com.skywalker.active.service.TravelNotesService
import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.ServerMessage
import com.skywalker.core.response.SuccessResponse
import com.skywalker.core.utils.BaseTools
import com.skywalker.core.utils.BaseUtils
import com.skywalker.travelnotes.dto.TravelNotesDTO
import com.skywalker.travelnotes.dto.TravelNotesParamDTO
import com.skywalker.travelnotes.form.TravelNotesForm
import com.skywalker.travelnotes.form.TravelNotesMessageForm
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
    private val jwtTokenUtil: JwtTokenUtil,
    private val baseTools: BaseTools
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
        @RequestParam(value = "size", required = false) size: Int?,
        @RequestParam(value = "time") time: Long
    ): SuccessResponse {
        val param = TravelNotesParamDTO()
        param.date = Date(time)
        var pageable = PageRequest(0, size ?: 5)
        return SuccessResponse(travelNotesService.listAll(param, if (null != size) pageable else null))
    }

    /**
     * 我的游记列表
     */
    @GetMapping("/travelNotes/myTravelNotes")
    fun myTravelNotes(
        @RequestParam(value = "size", required = false) size: Int?,
        @RequestParam(value = "time") time: Long,
        request: HttpServletRequest
    ): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        val param = TravelNotesParamDTO()
        param.postUserId = userId
        param.date = Date(time)
        val pageable = PageRequest(0, size ?: 5)
        return SuccessResponse(travelNotesService.listAll(param, if (null != size) pageable else null))
    }

    /**
     * 其他人员游记列表
     */
    @GetMapping("/{userId}/travelNotes")
    fun otherTravelNotes(
        @RequestParam(value = "size", required = false) size: Int?,
        @RequestParam(value = "time") time: Long,
        @PathVariable userId: Long
    ): SuccessResponse {
        val param = TravelNotesParamDTO()
        param.postUserId = userId
        val sort = Sort(Sort.Direction.DESC, "timeCreate")
        val pageable = PageRequest(0, size ?: 5, sort)
        return SuccessResponse(travelNotesService.listAll(param, if (null != size) pageable else null))
    }

    /**
     * 游记详情
     */
    @GetMapping("/travelNotes/{travelNotesId}")
    fun travelNotesInfo(
        @PathVariable travelNotesId: Long
    ): SuccessResponse {
        val param = TravelNotesParamDTO()
        param.travelNotesId = travelNotesId
        val map = travelNotesService.listAll(param, null)
        val list = map?.get("list") as List<TravelNotesDTO>?
        return SuccessResponse(list?.get(0))
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

    /**
     * 游记留言列表
     */
    @GetMapping("/travelNotes/{travelNotesId}/travelNotesMsg")
    fun listMsg(
        @RequestParam(value = "size", required = false) size: Int?,
        @RequestParam(value = "time") time: Long,
        @PathVariable travelNotesId: Long?
    ): SuccessResponse {
        var page: HashMap<String, Any?>?
        page = if (null != size) {
            val pageable = PageRequest(0, size)
            travelNotesService.listTravelNotesMsgByTravelNotesId(travelNotesId, time, pageable)
        } else {
            travelNotesService.listTravelNotesMsgByTravelNotesId(travelNotesId, time, null)
        }
        return SuccessResponse(page)
    }

    /**
     * 游记留言
     */
    @PostMapping("/travelNotes/{travelNotesId}/travelNotesMsg")
    fun createMsg(
        @Valid @RequestBody params: TravelNotesMessageForm,
        result: BindingResult,
        @PathVariable travelNotesId: Long,
        request: HttpServletRequest
    ): SuccessResponse {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.fieldErrors)
        }
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        params.userId = userId
        params.travelNotesId = travelNotesId

        val result = travelNotesService.createMsg(params)
        val userName = travelNotesService.getUserNameByTravelNotesId(travelNotesId)
        if (null != userName) {
            baseTools.convertAndSendToUser(
                userName,
                ServerMessage("游记留言", "你发布的游记有用户留言")
            )
        }
        return SuccessResponse(result)
    }
}