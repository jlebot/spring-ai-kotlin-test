package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.model.McpToolDefinition
import com.example.jlebot.springaikotlintest.services.McpService
import org.springframework.ai.tool.ToolCallback
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class McpApi(
    private val service: McpService
) {

    @GetMapping("/mcp/tools")
    fun listMcpTools(): ResponseEntity<List<McpToolDefinition>> {
        return ok(service.listMcpTools())
    }

}