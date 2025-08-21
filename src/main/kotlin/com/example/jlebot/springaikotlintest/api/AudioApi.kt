package com.example.jlebot.springaikotlintest.api

import com.example.jlebot.springaikotlintest.model.TextToConvert
import com.example.jlebot.springaikotlintest.services.AudioService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
class AudioApi(
    private val service: AudioService
) {

    @PostMapping("/transcribe")
    fun transcribe(
        @RequestParam("audioFile") audioFile: MultipartFile,
    ): ResponseEntity<String> {
        return ok(service.transcribe(audioFile))
    }

    @PostMapping("/tts")
    fun textToSpeech(
        @RequestBody text: TextToConvert
    ): ResponseEntity<ByteArray> {
        return ok(service.textToSpeech(text.value))
    }

}