package dev.leonardpark.chatgpt.repository.network.service

import dev.leonardpark.chatgpt.model.network.request.GenerateAudioRequestBody
import dev.leonardpark.chatgpt.model.network.request.GenerateImageRequestBody
import dev.leonardpark.chatgpt.model.network.request.GenerateMessagesRequestBody
import dev.leonardpark.chatgpt.model.network.response.GenerateImageResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface OpenAIService {

    @POST("chat/completions")
    @Streaming
    fun generateMessagesStream(@Body body: GenerateMessagesRequestBody): Call<ResponseBody>

    @POST("images/generations")
    suspend fun generateImage(@Body body: GenerateImageRequestBody): GenerateImageResponse

    @POST("audio/speech")
    @Streaming
    suspend fun generateAudio(@Body body: GenerateAudioRequestBody): Response<ResponseBody>
}