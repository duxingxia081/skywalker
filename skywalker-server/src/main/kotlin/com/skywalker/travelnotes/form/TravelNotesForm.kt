package com.skywalker.travelnotes.form

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotBlank

data class TravelNotesForm(
    @field:NotBlank(message = "游记标题不能为空")
    var title: String? = null,
    var postUserId: Long = 0L,
    @field:NotBlank(message = "游记内容内容不能为空")
    var content: String? = null,
    @field:NotBlank(message = "游记地点不能为空")
    var addressName: String? = null,
    var file: List<MultipartFile>? = null
)
