package com.example.jlebot.springaikotlintest.services

import com.example.jlebot.springaikotlintest.model.McpToolDefinition

interface McpService {

    fun listMcpTools() : List<McpToolDefinition>

    fun getVideoInfos(url: String): String

}