package com.skywalker.user.web

import com.skywalker.auth.utils.JwtTokenUtil
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.service.UserService
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService,private val JwtTokenUtil:JwtTokenUtil){
    @PostMapping
    fun create(@RequestBody params: SkywalkerUserDTO, bindingResult: BindingResult):Any{
        if (bindingResult.hasErrors()) {
            val sb = StringBuffer()
            for (objectError in bindingResult.allErrors) {
                sb.append((objectError as FieldError).field + " : ").append(objectError.getDefaultMessage())
            }
            return sb.toString()
        } else {
            return userService.create(params)
        }
    }

    @RequestMapping(value = "/myinfo", method = arrayOf(RequestMethod.GET))
    fun myInfo(request: HttpServletRequest): SkywalkerUserDTO{
        val userId = JwtTokenUtil.getUserIdFromToken(request) ?: throw ServiceException(ErrorConstants.ERROR_CODE_1104,ErrorConstants.ERROR_MSG_1104)
        return userService.findById(userId)
    }
}