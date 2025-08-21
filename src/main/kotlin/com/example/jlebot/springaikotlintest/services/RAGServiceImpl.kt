package com.example.jlebot.springaikotlintest.services

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.document.Document
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.ai.transformer.splitter.TextSplitter
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service


@Service
class RAGServiceImpl(
    private val chatModel: ChatModel,
    private val vectorStore: VectorStore,
    private val jdbcChatMemoryRepository: JdbcChatMemoryRepository,
) : RAGService {

    private val chatMemory = MessageWindowChatMemory.builder()
        .chatMemoryRepository(jdbcChatMemoryRepository)
        .maxMessages(10) // Set the maximum number of messages to retain in memory
        .build()

    private val chatClient = ChatClient.builder(chatModel)
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build()
        ).build()

    @Value("classpath:/templates/rag-system-message.st")
    private lateinit var ragSystemMessage: Resource

    @Value("classpath:/templates/rag-prompt-template.st")
    private lateinit var ragPromptTemplate: Resource

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

    override fun getAnswerWithManualVectorStoreSearch(question: String): String {
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
                "documents" to documents.mapNotNull { it.text }.joinToString("\n")
            )
        )

        val response = chatClient
            .prompt()
            .messages(listOf(systemMessage, userMessage))
            .call()
        return response.content().orEmpty()
    }

    override fun getAnswerWithAutoVectorStoreSearch(question: String): String {
        val response = chatClient
            .prompt()
            .system(ragSystemMessage)
            .user(question)
            .advisors(
                QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(
                        SearchRequest.builder()
                            .query(question)
                            .topK(5)
                            .build()
                    )
                    .build()
            )
            .call()
        return response.content().orEmpty()
    }

}