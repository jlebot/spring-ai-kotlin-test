package com.example.jlebot.springaikotlintest.services

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.stereotype.Service


@Service
class OpenAIServiceImpl(
    private val chatModel: ChatModel,
) : OpenAIService {

    private val chatClient = ChatClient.create(chatModel)

    override fun getAnswer(question: String): String {
        val promptTemplate = PromptTemplate(question)
        val prompt = promptTemplate.create()

        val response = chatClient.prompt(prompt).call()
        return response.content().orEmpty()
    }

}