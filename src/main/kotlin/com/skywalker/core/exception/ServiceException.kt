package com.skywalker.core.exception

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

}
