package com.example.jlebot.springaikotlintest.services

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class OpenAIServiceImpl(
    private val chatModel: ChatModel,
    private val transcriptionModel: OpenAiAudioTranscriptionModel,
) : OpenAIService {

    private val chatClient = ChatClient.create(chatModel)

    override fun getAnswer(question: String): String {
        val promptTemplate = PromptTemplate(question)
        val prompt = promptTemplate.create()

        val response = chatClient.prompt(prompt).call()
        return response.content().orEmpty()
    }

    override fun transcribe(audioFile: MultipartFile): String {
        val prompt = AudioTranscriptionPrompt(
            audioFile.resource,
            OpenAiAudioTranscriptionOptions
                .builder()
                .prompt("Transcris le fichier audio joint en texte")
                .build()
        )
        val response = transcriptionModel.call(prompt)
        return response.result.output
    }

}