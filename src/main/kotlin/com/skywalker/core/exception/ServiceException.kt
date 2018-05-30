package com.skywalker.core.exception

import org.springframework.validation.FieldError

/**
 * 异常类
 */
class ServiceException : Exception {

    var errorKey: String = "-1000"
    var value: String = "系统错误"

    constructor() : super()

    constructor(errorKey: String, message: String) : super(message) {
        this.errorKey = errorKey
        this.value = message
    }
    constructor(errorKey: String, message: String,e:Exception) : super(message) {
        e.printStackTrace()
        this.errorKey = errorKey
        this.value = message
    }
    constructor(errorKey: String, fieldErrors: List<FieldError>){
        this.errorKey = errorKey
        val sb = StringBuffer()
        for (error in fieldErrors) {
            sb.append(error.defaultMessage).append(";")
        }
        this.value = sb.toString()
    }

}
