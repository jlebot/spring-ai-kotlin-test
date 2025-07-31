package com.example.jlebot.springaikotlintest.services

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface OpenAIService {

    fun getAnswer(question: String) : String

    fun transcribe(audioFile: MultipartFile) : String

    fun loadToVectorStore(documents: List<Resource>)

    fun getAnswerFromVectorStore(question: String) : String

}