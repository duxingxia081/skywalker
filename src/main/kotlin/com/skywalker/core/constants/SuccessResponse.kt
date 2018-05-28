package com.skywalker.core.constants

data class SuccessResponse(
    val data:Any?
)
{
    val code: String=ErrorConstants.SUCCESS_CODE_0
    val message: String=ErrorConstants.SUCCESS_MSG_0
}
