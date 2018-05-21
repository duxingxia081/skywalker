package com.skywalker.core.web

import com.skywalker.core.exception.ErrorResponse
import com.skywalker.core.exception.ServiceException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

@ControllerAdvice
@RestController
class GlobalExceptionHandler {
    @ExceptionHandler(value = ServiceException::class)
    fun handleException(e: ServiceException): ErrorResponse {
        e.printStackTrace()
        return ErrorResponse(e.errorKey, e.value)
    }
    @ExceptionHandler(value = Exception::class)
    fun exception(e: Exception): ErrorResponse {
        e.printStackTrace()
        return ErrorResponse("-1000", "系统错误")
    }
}




