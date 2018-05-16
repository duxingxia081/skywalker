package com.skywalker.user.web

import com.skywalker.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService){
    @GetMapping("/findUser/{userId}")
    fun findByUserId(@PathVariable userId:String)
            = userService.findAll()
}