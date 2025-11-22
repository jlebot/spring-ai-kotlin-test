package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.model.McpToolDefinition
import com.example.jlebot.springaikotlintest.services.McpService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class McpApi(
    private val service: McpService
) {

    @GetMapping("/mcp/tools/youtube/infos")
    fun getYoutubeVideoInfos(
        @RequestParam url: String
    ): ResponseEntity<String> {
        return ok(service.getVideoInfos(url))
    }

    @GetMapping("/mcp/tools")
    fun listMcpTools(): ResponseEntity<List<McpToolDefinition>> {
        return ok(service.listMcpTools())
    }

}