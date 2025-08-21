package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.model.Question
import com.example.jlebot.springaikotlintest.model.VacationDestination
import com.example.jlebot.springaikotlintest.services.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class ChatApi(
    private val service: ChatService
) {

    @PostMapping("/ask")
    fun getAnswer(
        @RequestBody question: Question
    ): ResponseEntity<String> {
        return ok(service.getAnswer(question.value))
    }

    @PostMapping("/vacation-destination/ask")
    fun getDestinationsFor(
        @RequestBody question: Question
    ): ResponseEntity<List<VacationDestination>> {
        return ok(service.getDestinationsFor(question.value))
    }

    @PostMapping("/ask-with-tools")
    fun askWithTools(
        @RequestBody question: Question
    ): ResponseEntity<String> {
        return ok(service.askWithTools(question.value))
    }

}