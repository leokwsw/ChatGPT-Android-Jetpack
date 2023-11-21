package dev.leonardpark.chatgpt.model.network.response.base

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
    @SerializedName("error") val error: OpenAIError
)

@Keep
data class OpenAIError(
    @SerializedName("message") val message: String
)
