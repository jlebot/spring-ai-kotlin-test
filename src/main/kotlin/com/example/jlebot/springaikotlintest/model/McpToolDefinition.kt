package com.example.jlebot.springaikotlintest.model

import com.fasterxml.jackson.databind.JsonNode

data class McpToolDefinition(
    val name: String,
    val description: String,
    val inputSchema: JsonNode
)
