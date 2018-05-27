package com.skywalker.core.web

import com.skywalker.user.dto.SkywalkerUserDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Paths
import java.io.IOException
import java.nio.file.Files
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@RequestMapping("/img")
class ImageController(private var resourceLoader: ResourceLoader) {
    @Value("\${app.img.root}")
    private val ROOT: String? = null

    @GetMapping("/{file}/{filename:.+}")
    fun getFile(@PathVariable file: String, @PathVariable filename: String): ResponseEntity<*>? {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT + file + "/", filename).toString()))
        } catch (e: Exception) {
            return null
        }

    }

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/fileUpload")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, @Valid @RequestBody params: SkywalkerUserDTO,
                         redirectAttributes: RedirectAttributes, request: HttpServletRequest): String {
        System.out.println(request.getParameter("member"))
        if (!file.isEmpty) {
            try {
                Files.copy(file.inputStream, Paths.get(ROOT, file.originalFilename))
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + file.originalFilename + "!")
            } catch (e: IOException) {
                redirectAttributes.addFlashAttribute("message", "Failued to upload " + file.originalFilename + " => " + e.message)
            } catch (e: RuntimeException) {
                redirectAttributes.addFlashAttribute("message", "Failued to upload " + file.originalFilename + " => " + e.message)
            }

        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.originalFilename + " because it was empty")
        }

        return "redirect:/"
    }
}