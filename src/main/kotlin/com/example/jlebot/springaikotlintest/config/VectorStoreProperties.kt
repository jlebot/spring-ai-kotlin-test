package com.example.jlebot.springaikotlintest.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
@ConfigurationProperties(prefix = "rag-test")
class VectorStoreProperties {
    var enabled: Boolean = false
    var documentsToLoad: List<Resource> = listOf()
}
