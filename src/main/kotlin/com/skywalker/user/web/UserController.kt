package com.skywalker.user.web

import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.dto.UserDTO
import com.skywalker.user.service.UserService
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService, private val JwtTokenUtil: JwtTokenUtil) {
    @PostMapping
    fun create(@Valid @RequestBody params: SkywalkerUserDTO, result: BindingResult): Any {
        if (result.hasErrors()) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1106, result.getFieldErrors())
        }
        return userService.create(params)
    }

    @RequestMapping(value = "/myinfo", method = arrayOf(RequestMethod.GET))
    fun myInfo(request: HttpServletRequest): UserDTO {
        val userId = JwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(
            ErrorConstants.ERROR_CODE_1104,
            ErrorConstants.ERROR_MSG_1104
        )
        return userService.findById(userId)
    }
}