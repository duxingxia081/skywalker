package com.skywalker.user.web

import com.skywalker.user.dto.SkywalkerUserDTO
import com.skywalker.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService){
    fun create(@Valid @RequestBody params: SkywalkerUserDTO): SkywalkerUserDTO {
        return userService.create(params)
    }
    @GetMapping("/findUser/{userId}")
    fun findByUserId(@PathVariable userId:String) : SkywalkerUserDTO
    {
        val dto = userService.findByUserName("weizh")
            return dto
    }
}