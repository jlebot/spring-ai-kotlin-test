package com.example.jlebot.springaikotlintest.services

interface OpenAIService {

    fun getAnswer(question: String) : String

}