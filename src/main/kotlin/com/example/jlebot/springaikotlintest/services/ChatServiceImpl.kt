package com.example.jlebot.springaikotlintest.services

import com.example.jlebot.springaikotlintest.model.VacationDestination
import com.example.jlebot.springaikotlintest.tools.MyTasksTools
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service


@Service
class ChatServiceImpl(
    private val chatModel: ChatModel,
    private val jdbcChatMemoryRepository: JdbcChatMemoryRepository,
    private val myTasksTools: MyTasksTools
) : ChatService {

    private val chatMemory = MessageWindowChatMemory.builder()
        .chatMemoryRepository(jdbcChatMemoryRepository)
        .maxMessages(10) // Set the maximum number of messages to retain in memory
        .build()

    private val chatClient = ChatClient.builder(chatModel)
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build()
        ).build()

    override fun getAnswer(question: String): String {
        val promptTemplate = PromptTemplate(question)
        val prompt = promptTemplate.create()

        val response = chatClient.prompt(prompt).call()
        return response.content().orEmpty()
    }

    override fun getDestinationsFor(criteria: String): List<VacationDestination> {
        val systemMessage = SystemPromptTemplate(
            "Tu es un assistant virtuel spécialisé dans les destinations de vacances. "
        ).createMessage()

        val userMessage = PromptTemplate(
            "Liste les destinations de vacances en fonction des critères suivants :\n {criteria}.\n " +
                    "La liste doit contenir au maximum 5 destinations."
        ).createMessage(mapOf("criteria" to criteria))

        val response = chatClient.prompt(Prompt(listOf(systemMessage, userMessage)))
            .call()
            .entity(
                object : ParameterizedTypeReference<List<VacationDestination>>() {}
            )
        return response.orEmpty()
    }

    override fun askWithTools(question: String): String {
        val response = chatClient
            .prompt()
            .system("Tu es un assistant virtuel qui peut répondre à des questions et exécuter des commandes.")
            .user(question)
            .tools(myTasksTools)
            .call()
        return response.content().orEmpty()
    }

}