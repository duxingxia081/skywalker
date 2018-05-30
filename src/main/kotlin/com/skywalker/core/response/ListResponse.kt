package com.skywalker.core.response

import com.skywalker.core.constants.ErrorConstants

data class ListResponse(
    val total: Long?,
    val data: List<Any>?
) {
    val code: String = ErrorConstants.SUCCESS_CODE_0
    val message: String = ErrorConstants.SUCCESS_MSG_0
}