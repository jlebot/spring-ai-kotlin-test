package com.example.jlebot.springaikotlintest.services

import com.example.jlebot.springaikotlintest.model.McpToolDefinition
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider
import org.springframework.stereotype.Service


@Service
class McpServiceImpl(
    private val chatModel: ChatModel,
    private val mcpTools: SyncMcpToolCallbackProvider,
) : McpService {

    private val chatClient = ChatClient.builder(chatModel)
        .defaultToolCallbacks(mcpTools)
        .build()

    override fun listMcpTools(): List<McpToolDefinition> {
        return mcpTools.toolCallbacks
            .map { it.toolDefinition }
            .map {
                McpToolDefinition(
                    name = it.name(),
                    description = it.description(),
                    inputSchema =  ObjectMapper().readTree(it.inputSchema()),
                )
            }.toList()
    }

}