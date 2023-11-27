package dev.leonardpark.chatgpt.repository.network

import com.google.gson.Gson
import dev.leonardpark.chatgpt.database.entity.ChatMessage
import dev.leonardpark.chatgpt.model.network.request.GenerateAudioRequestBody
import dev.leonardpark.chatgpt.model.network.request.GenerateImageRequestBody
import dev.leonardpark.chatgpt.model.network.request.GenerateMessagesRequestBody
import dev.leonardpark.chatgpt.model.network.response.GenerateMessagesResponse
import dev.leonardpark.chatgpt.repository.base.BaseRepository
import dev.leonardpark.chatgpt.repository.network.service.OpenAIService
import dev.leonardpark.chatgpt.store.datastore.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenAIRepository @Inject constructor(
    private val openAIService: OpenAIService,
    private val settingsDataStore: SettingsDataStore,
    private val gson: Gson
): BaseRepository() {

    suspend fun generateAudio(
        model: String,
        input: String,
        voice: String,
        format: String,
        speed: Float
    ) = openAIService.generateAudio(
        GenerateAudioRequestBody(
            model = model,
            input = input,
            voice = voice,
            responseFormat = format,
            speed = speed
        )
    ).apply {
        if (errorBody() != null) {
            throw HttpException(this)
        }
    }.body()

    suspend fun generateImage(model: String, prompt: String) = openAIService.generateImage(
        GenerateImageRequestBody(model, prompt)
    ).data.first().url

    suspend fun generateMessagesStream(messages: List<ChatMessage>) = flow {
        val response = openAIService.generateMessagesStream(
            GenerateMessagesRequestBody(
                messages,
                settingsDataStore.model.first(),
                settingsDataStore.temperature.first()
            )
        ).execute()

        if (response.errorBody() != null) {
            throw HttpException(response)
        }

        response.body()?.byteStream()?.bufferedReader()?.use { reader ->
            val contents = mutableMapOf<Int, String>()

            while (currentCoroutineContext().isActive) {
                val line = reader.readLine() ?: continue
                if (line.isEmpty()) continue

                if (line == "data: [DONE]") break

                val lineResponse = gson.fromJson(
                    line.dropWhile { it != '{' }, // dropping "data: "
                    GenerateMessagesResponse::class.java
                )

                lineResponse.choices.firstOrNull()?.also { choice ->
                    choice.delta?.content?.takeIf { it.isNotEmpty() }?.also { content ->
                        var currentContent = contents[choice.index] ?: ""
                        currentContent += content

                        contents[choice.index] = currentContent

                        emit(choice.index to currentContent)
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}