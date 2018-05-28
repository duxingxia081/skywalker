package com.skywalker.core.utils

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

}