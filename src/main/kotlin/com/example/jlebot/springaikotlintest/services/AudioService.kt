package com.example.jlebot.springaikotlintest.services

import org.springframework.web.multipart.MultipartFile

interface AudioService {

    fun transcribe(audioFile: MultipartFile) : String

    fun textToSpeech(text: String) : ByteArray

}