package com.skywalker.core.exception

data class ErrorResponse(
        val code: String,
        val message: String
) {
    constructor(code: String, message: String, data: Any) : this(code, message)

}