package com.example.jlebot.springaikotlintest.bootstrap

import com.example.jlebot.springaikotlintest.config.VectorStoreProperties
import com.example.jlebot.springaikotlintest.services.RAGService
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class LoadVectorStore(
    private val service: RAGService,
    private val vectorStore: VectorStore,
    private val vectorStoreProperties: VectorStoreProperties
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (vectorStore.shouldBeLoaded()) {
            println("Loading documents into vector store")

            service.loadToVectorStore(
                documents = vectorStoreProperties.documentsToLoad
            )

            println("Vector store loaded")
        } else {
            println("Vector store disabled or already loaded")
        }
    }

    private fun VectorStore.shouldBeLoaded() =
        vectorStoreProperties.enabled &&
            this.similaritySearch("Malicio 3").isEmpty()
}

