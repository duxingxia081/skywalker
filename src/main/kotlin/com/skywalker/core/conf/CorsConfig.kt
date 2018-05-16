import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class SkywalkerApplication

fun buildConfig(): CorsConfiguration {
    val corsConfiguration = CorsConfiguration()
    corsConfiguration.addAllowedOrigin("*") // 允许任何域名使用
    corsConfiguration.addAllowedHeader("*") // 允许任何头
    corsConfiguration.addAllowedMethod("*") // 允许任何方法（post、get等）
    corsConfiguration.allowCredentials = true
    return corsConfiguration
}

@Bean
fun corsFilter(): CorsFilter {
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", buildConfig()) // 4
    return CorsFilter(source)
}