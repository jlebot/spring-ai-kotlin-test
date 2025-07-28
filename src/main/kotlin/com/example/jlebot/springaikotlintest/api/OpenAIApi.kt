package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.model.Question
import com.example.jlebot.springaikotlintest.services.OpenAIService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class OpenAIApi(
    private val service: OpenAIService
) {

    @PostMapping("/ask")
    fun getAnswer(
        @RequestBody question: Question
    ): ResponseEntity<String> {
        return ok(service.getAnswer(question.value))
    }

}