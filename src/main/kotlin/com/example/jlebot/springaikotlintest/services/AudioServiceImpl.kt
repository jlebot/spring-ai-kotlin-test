package com.example.jlebot.springaikotlintest.services

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions
import org.springframework.ai.openai.audio.speech.SpeechModel
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class AudioServiceImpl(
    private val speechModel: SpeechModel,
    private val transcriptionModel: OpenAiAudioTranscriptionModel,
) : AudioService {

    override fun textToSpeech(text: String): ByteArray {
        return speechModel.call(text)
    }

    override fun transcribe(audioFile: MultipartFile): String {
        val prompt = AudioTranscriptionPrompt(
            audioFile.resource,
            OpenAiAudioTranscriptionOptions
                .builder()
                .prompt("Transcris le fichier audio joint en texte")
                .build()
        )
        val response = transcriptionModel.call(prompt)
        return response.result.output
    }

}