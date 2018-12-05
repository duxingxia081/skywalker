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
                        .allowedOrigins("http://localhost:8100")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT")
            }
        }
    }
}