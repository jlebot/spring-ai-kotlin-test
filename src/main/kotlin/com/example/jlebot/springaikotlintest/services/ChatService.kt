package com.example.jlebot.springaikotlintest.services

import com.example.jlebot.springaikotlintest.model.VacationDestination

interface ChatService {

    fun getAnswer(question: String) : String

    fun getDestinationsFor(criteria: String): List<VacationDestination>

    fun askWithTools(question: String) : String

}