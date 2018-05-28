package com.skywalker.user.web

import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.constants.ErrorResponse
import com.skywalker.core.constants.SuccessResponse
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.utils.BaseTools
import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.dto.UserDTO
import com.skywalker.user.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService, private val jwtTokenUtil: JwtTokenUtil,private val baseTools: BaseTools) {
    @Value("\${app.img.head}")
    private val headImgPath: String=""

    @PostMapping
    fun create(@Valid @RequestBody params: SkywalkerUserDTO, result: BindingResult): SuccessResponse {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.fieldErrors)
        }
        return SuccessResponse(userService.create(params))
    }
    @PutMapping
    fun update(@RequestBody params: SkywalkerUserDTO,request: HttpServletRequest): SuccessResponse{
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        params.userId=userId
        return SuccessResponse(userService.update(params))
    }
    @RequestMapping(value = "/myinfo", method = [(RequestMethod.GET)])
    fun myInfo(request: HttpServletRequest): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        return SuccessResponse(userService.findById(userId))
    }
    @RequestMapping(method = [(RequestMethod.POST)], value = "/headImg")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile?,request: HttpServletRequest): Any {
        if (null!=file&&!file.isEmpty) {
            try {
                val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                        ErrorConstants.ERROR_CODE_1104,
                        ErrorConstants.ERROR_MSG_1104
                )
                val fileName = userId.toString()+"."+file.originalFilename!!.substringAfterLast(".")
                baseTools.upLoad(file,headImgPath,fileName)
                return SuccessResponse(
                    "heads/$fileName"
                )
            } catch (e: IOException) {
                throw ServiceException(ErrorConstants.ERROR_CODE_1,ErrorConstants.ERROR_MSG_1)
            }

        } else {
            return ErrorResponse(
                ErrorConstants.SUCCESS_CODE_0,
                ErrorConstants.SUCCESS_MSG_0_
            )
        }
    }


}