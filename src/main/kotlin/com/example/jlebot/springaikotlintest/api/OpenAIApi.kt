package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.model.Question
import com.example.jlebot.springaikotlintest.model.VacationDestination
import com.example.jlebot.springaikotlintest.services.OpenAIService
import org.springframework.ai.chat.messages.Message
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


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

    @PostMapping("/vacation-destination/ask")
    fun getDestinationsFor(
        @RequestBody question: Question
    ): ResponseEntity<List<VacationDestination>> {
        return ok(service.getDestinationsFor(question.value))
    }

    @PostMapping("/transcribe")
    fun transcribe(
        @RequestParam("audioFile") audioFile: MultipartFile,
    ): ResponseEntity<String> {
        return ok(service.transcribe(audioFile))
    }

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

    @GetMapping("/sessions")
    fun listAllChats(): ResponseEntity<Map<String, List<Message>>> {
        return ok(service.listAllChats())
    }

    @PostMapping("/ask-with-tools")
    fun askWithTools(
        @RequestBody question: Question
    ): ResponseEntity<String> {
        return ok(service.askWithTools(question.value))
    }

}