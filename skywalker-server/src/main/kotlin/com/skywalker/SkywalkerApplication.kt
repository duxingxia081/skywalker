package com.skywalker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SkywalkerApplication

fun main(args: Array<String>) {
    runApplication<SkywalkerApplication>(*args)
}
