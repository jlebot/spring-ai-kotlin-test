package com.example.jlebot.springaikotlintest.services

import org.springframework.ai.chat.messages.Message

interface MemoryService {

    fun listAllChats() : Map<String, List<Message>>

}