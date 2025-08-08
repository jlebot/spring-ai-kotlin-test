package com.example.jlebot.springaikotlintest.tools

import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Component

@Component
class MyTasksTools {

    @Tool(description = "Récupère le statut d'une tache")
    fun getStatus(
        @ToolParam(description = "Nom de la tache") taskName: String
    ): String {
        println("Calling MyTools.getMyTaskStatus($taskName)")
        return "Le statut de la tache $taskName est 'En cours'."
    }

    @Tool(description = "Supprime une tache")
    fun deleteTask(
        @ToolParam(description = "Nom de la tache") taskName: String
    ) {
        println("Calling MyTools.deleteTask($taskName)")
    }

}