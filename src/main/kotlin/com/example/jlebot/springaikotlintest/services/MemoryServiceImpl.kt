package com.example.jlebot.springaikotlintest.services

import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.ai.chat.messages.Message
import org.springframework.stereotype.Service


@Service
class MemoryServiceImpl(
    private val jdbcChatMemoryRepository: JdbcChatMemoryRepository,
) : MemoryService {

    override fun listAllChats(): Map<String, List<Message>> {
        return jdbcChatMemoryRepository.findConversationIds()
            .associateWith { jdbcChatMemoryRepository.findByConversationId(it) }
    }

}