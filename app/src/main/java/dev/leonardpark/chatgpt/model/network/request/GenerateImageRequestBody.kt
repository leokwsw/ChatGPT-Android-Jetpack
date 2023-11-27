package dev.leonardpark.chatgpt.model.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GenerateImageRequestBody(
    @SerializedName("model") val model: String = "dall-e-3",
    @SerializedName("prompt") val prompt: String,
    @SerializedName("size") val size: String = "1024x1024"
)