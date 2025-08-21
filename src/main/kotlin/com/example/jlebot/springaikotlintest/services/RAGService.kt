package com.example.jlebot.springaikotlintest.services

import org.springframework.core.io.Resource

interface RAGService {

    fun loadToVectorStore(documents: List<Resource>)

    fun getAnswerWithManualVectorStoreSearch(question: String) : String

    fun getAnswerWithAutoVectorStoreSearch(question: String) : String

}