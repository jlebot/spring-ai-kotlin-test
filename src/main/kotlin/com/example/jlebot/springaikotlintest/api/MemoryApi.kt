package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.services.MemoryService
import org.springframework.ai.chat.messages.Message
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class MemoryApi(
    private val service: MemoryService
) {

    @GetMapping("/sessions")
    fun listAllChats(): ResponseEntity<Map<String, List<Message>>> {
        return ok(service.listAllChats())
    }

}