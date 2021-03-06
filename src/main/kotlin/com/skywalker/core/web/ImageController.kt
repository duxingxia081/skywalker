package com.skywalker.core.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Paths


@RestController
@RequestMapping("/img")
class ImageController(private var resourceLoader: ResourceLoader) {
    @Value("\${app.img.root}")
    private val ROOT: String? = null

    @GetMapping("/{file}/{filename:.+}")
    fun getFile(@PathVariable file: String, @PathVariable filename: String): ResponseEntity<*>? {

        return try {
            ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT + file + "/", filename).toString()))
        } catch (e: Exception) {
            null
        }

    }
}