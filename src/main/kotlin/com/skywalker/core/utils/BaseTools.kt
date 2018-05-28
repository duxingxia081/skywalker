package com.skywalker.core.utils

import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Component
class BaseTools {
    /**
     * 上传
     */
    fun upLoad(file: MultipartFile, path: String,fileName:String)
    {
        Files.copy(file.inputStream, Paths.get(path, fileName), StandardCopyOption.REPLACE_EXISTING)
    }

    /**
     * 检查上传类型
     */
    fun checkImgType(file: MultipartFile,suffixList:String)
    {
        val suffix = file?.originalFilename?.substringAfterLast(".")
        if (null==suffix||!suffixList.contains(suffix.trim().toLowerCase())) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1003,ErrorConstants.ERROR_MSG_1003+",只支持$suffixList")
        }
    }

}