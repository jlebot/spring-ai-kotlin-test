package com.example.jlebot.springaikotlintest.services

import com.example.jlebot.springaikotlintest.model.VacationDestination
import org.springframework.ai.chat.messages.Message
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface OpenAIService {

    fun getAnswer(question: String) : String

    fun getDestinationsFor(criteria: String): List<VacationDestination>

    fun transcribe(audioFile: MultipartFile) : String

    fun loadToVectorStore(documents: List<Resource>)

    fun getAnswerWithManualVectorStoreSearch(question: String) : String

    fun getAnswerWithAutoVectorStoreSearch(question: String) : String

    fun listAllChats() : Map<String, List<Message>>

    fun askWithTools(question: String) : String

    fun textToSpeech(text: String) : ByteArray
}