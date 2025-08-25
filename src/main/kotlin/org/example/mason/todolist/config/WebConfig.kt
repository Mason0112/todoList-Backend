package org.example.mason.todolist.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Value("\${cors.allowed-origins}") private val allowedOrigins: Array<String>
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**") // 套用規則到所有 /api/ 開頭的路徑
            .allowedOrigins(*allowedOrigins) // 從 application.properties 讀取允許的來源
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
            .allowedHeaders("*") // 允許所有標頭
            .allowCredentials(true) // 允許傳送 cookies
    }
}