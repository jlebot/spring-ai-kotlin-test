package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.model.Question
import com.example.jlebot.springaikotlintest.services.RAGService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class RAGApi(
    private val service: RAGService
) {

    @PostMapping("/vector-store/ask")
    fun getAnswerFromVectorStore(
        @RequestParam("auto") auto: Boolean = true,
        @RequestBody question: Question
    ): ResponseEntity<String> {
        return if (auto)
            ok(service.getAnswerWithAutoVectorStoreSearch(question.value))
        else
            ok(service.getAnswerWithManualVectorStoreSearch(question.value))
    }

}