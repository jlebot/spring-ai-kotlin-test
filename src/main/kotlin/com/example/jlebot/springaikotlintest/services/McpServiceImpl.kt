package com.example.jlebot.springaikotlintest.services

import com.example.jlebot.springaikotlintest.model.McpToolDefinition
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service




@Service
class McpServiceImpl(
    private val chatModel: ChatModel,
    private val mcpTools: SyncMcpToolCallbackProvider,
) : McpService {

    @Value("classpath:/templates/youtube-infos-system-message.st")
    private lateinit var systemMessage: Resource

    private val chatClient = ChatClient.builder(chatModel)
        .defaultToolCallbacks(mcpTools)
        .build()

    override fun getVideoInfos(url: String): String {
        val systemMessage = SystemPromptTemplate(systemMessage).createMessage()

        val response = chatClient
            .prompt()
            .messages(listOf(systemMessage))
            .user("Please get the video info at this URL: $url")
            .call()

        return response.content().orEmpty()
    }

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