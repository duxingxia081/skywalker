package com.skywalker.core.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class MyConfiguration {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry!!.addMapping("/**")
                        .allowedOrigins("http://192.168.0.133")
                        .allowCredentials(false)
                        .allowedMethods("GET", "POST", "PUT")
            }
        }
    }
}