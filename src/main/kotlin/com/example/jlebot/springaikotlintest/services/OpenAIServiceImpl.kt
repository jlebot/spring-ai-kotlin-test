package com.example.jlebot.springaikotlintest.services

import com.example.jlebot.springaikotlintest.model.VacationDestination
import com.example.jlebot.springaikotlintest.tools.MyTasksTools
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.document.Document
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions
import org.springframework.ai.openai.audio.speech.SpeechModel
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.ai.transformer.splitter.TextSplitter
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class OpenAIServiceImpl(
    private val chatModel: ChatModel,
    private val transcriptionModel: OpenAiAudioTranscriptionModel,
    private val speechModel: SpeechModel,
    private val vectorStore: VectorStore,
    private val jdbcChatMemoryRepository: JdbcChatMemoryRepository,
    private val myTasksTools: MyTasksTools
) : OpenAIService {

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

    override fun listAllChats(): Map<String, List<Message>> {
        return jdbcChatMemoryRepository.findConversationIds()
            .associateWith { jdbcChatMemoryRepository.findByConversationId(it) }
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

    override fun textToSpeech(text: String): ByteArray {
        return speechModel.call(text)
    }

}