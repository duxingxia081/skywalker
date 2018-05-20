package com.skywalker.user.web

import com.skywalker.base.bo.MhoSkywalkerUserDTO
import com.skywalker.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService){
    @PostMapping
    fun create(@Valid @RequestBody params: MhoSkywalkerUserDTO): MhoSkywalkerUserDTO {
        return userService.create(params)
    }
}