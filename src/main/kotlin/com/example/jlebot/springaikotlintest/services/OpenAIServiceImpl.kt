package com.example.jlebot.springaikotlintest.services

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.document.Document
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.ai.transformer.splitter.TextSplitter
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class OpenAIServiceImpl(
    private val chatModel: ChatModel,
    private val transcriptionModel: OpenAiAudioTranscriptionModel,
    private val vectorStore: VectorStore
) : OpenAIService {

    private val chatClient = ChatClient.create(chatModel)

    @Value("classpath:/templates/rag-system-message.st")
    private val ragSystemMessage: Resource? = null

    @Value("classpath:/templates/rag-prompt-template.st")
    private val ragPromptTemplate: Resource? = null

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

    override fun loadToVectorStore(documents: List<Resource>) {
        println("Loading ${documents.size} documents")
        documents.forEach { document ->
            println("Loading document: ${document.filename}")

            val documentReader = TikaDocumentReader(document)
            val documents: List<Document> = documentReader.get()

            val textSplitter: TextSplitter = TokenTextSplitter()
            val splitDocuments: List<Document> = textSplitter.apply(documents)

            vectorStore.add(splitDocuments)
        }
    }

    override fun getAnswerFromVectorStore(question: String): String {
        println("GetAnswerFromVectorStore, Question: $question")
        val documents: List<Document> = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(question)
                .topK(5)
                .build()
        )
        println("Number of relevant documents founded: ${documents.size}")

        val systemMessage = SystemPromptTemplate(ragSystemMessage).createMessage()
        val userMessage = PromptTemplate(ragPromptTemplate).createMessage(
            mapOf(
                "input" to question,
                "documents" to documents.mapNotNull { it.text }.joinToString("\n"))
        )

        val response = chatClient.prompt(
            Prompt(listOf(systemMessage, userMessage))
        ).call()
        return response.content().orEmpty()
    }

}