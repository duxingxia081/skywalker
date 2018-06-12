package com.skywalker.travelnotes.form

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class TravelNotesMessageForm(
    var travelNotesId: Long = 0L,
    var parentMessageId: Long? = null,
    var userId: Long = 0L,
    @field:NotBlank(message = "留言内容不能为空")
    @field:Length(max = 100, message = "最大长度不能超过100个文字")
    var content: String? = null
)