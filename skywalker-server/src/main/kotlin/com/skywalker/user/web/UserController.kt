package com.skywalker.user.web

import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.core.response.ErrorResponse
import com.skywalker.core.response.SuccessResponse
import com.skywalker.core.utils.BaseUtils
import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService, private val jwtTokenUtil: JwtTokenUtil, private val authenticationManager: AuthenticationManager) {
    @Value("\${app.img.head}")
    private val headImgPath: String = ""
    @Value("\${app.img.type}")
    private val suffixList: String = ""

    @PostMapping
    fun create(@Valid @RequestBody params: SkywalkerUserDTO, request: HttpServletRequest, result: BindingResult): SuccessResponse {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.fieldErrors)
        }

        val captcha = request.session.getAttribute("captcha")
        if (StringUtils.isEmpty(captcha) || captcha != params.captcha) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1115, ErrorConstants.ERROR_MSG_1115)
        }
        request.session.invalidate()
        return SuccessResponse(userService.create(params))
    }

    @PutMapping
    fun update(@RequestBody params: SkywalkerUserDTO, request: HttpServletRequest): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        params.userId = userId
        if (!StringUtils.isEmpty(params.oldPassword) && !StringUtils.isEmpty(params.userName)) {

            val dto: SkywalkerUserDTO? = userService.findByUserName(params.userName!!)
            if (null != dto) {
                if (!BCryptPasswordEncoder().matches(params.oldPassword, dto.password)) {
                    throw ServiceException(ErrorConstants.ERROR_CODE_1114, ErrorConstants.ERROR_MSG_1114)
                }
            }
        }
        return SuccessResponse(userService.update(params))
    }

    @GetMapping(value = "/myinfo")
    private fun myInfo(request: HttpServletRequest): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        return SuccessResponse(userService.findById(userId))
    }

    @GetMapping(value = "/headImg")
    private fun getHeadImg(request: HttpServletRequest): SuccessResponse {
        val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                ErrorConstants.ERROR_CODE_1104,
                ErrorConstants.ERROR_MSG_1104
        )
        return SuccessResponse(userService.findImgByUserId(userId))
    }

    @PostMapping(value = "/headImg")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile?, request: HttpServletRequest): Any {
        if (null != file && !file.isEmpty) {
            try {
                val userId = jwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
                        ErrorConstants.ERROR_CODE_1104,
                        ErrorConstants.ERROR_MSG_1104
                )
                val name = BaseUtils.fileUpLoad(file, headImgPath, suffixList)
                return SuccessResponse(userService.updateHead(userId, "img/heads/$name"))
            } catch (e: IOException) {
                throw ServiceException(ErrorConstants.ERROR_CODE_1, ErrorConstants.ERROR_MSG_1, e)
            }

        } else {
            return ErrorResponse(
                    ErrorConstants.SUCCESS_CODE_0,
                    ErrorConstants.SUCCESS_MSG_0_
            )
        }
    }


}