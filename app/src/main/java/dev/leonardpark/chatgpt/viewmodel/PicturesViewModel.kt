package dev.leonardpark.chatgpt.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonardpark.chatgpt.database.AppDatabase
import dev.leonardpark.chatgpt.database.entity.GeneratedImage
import dev.leonardpark.chatgpt.repository.network.OpenAIRepository
import dev.leonardpark.chatgpt.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PicturesViewModel @Inject constructor(
    private val openAIRepository: OpenAIRepository,
    private val database: AppDatabase,
    val imageLoader: ImageLoader,
    application: Application
): BaseViewModel(application) {
    var models = listOf("dall-e-3", "dall-e-2")
    var model by mutableStateOf("dall-e-3")
    var query by mutableStateOf("")
    var loading by mutableStateOf(false)

    val generatedImages = database.imagesDao().getAll()
        .map { it.asReversed() }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    fun generate() = viewModelScope.onIO({
        query.trim().also { query ->
            if (query.isEmpty()) return@onIO

            withContext(Dispatchers.Main) {
                loading = true
            }
            val imageUrl = openAIRepository.generateImage(model, query)

            withContext(Dispatchers.Main) {
                loading = false
            }
            database.imagesDao().insert(GeneratedImage(query, imageUrl))
        }
    }, errorBlock = { loading = false })

    @OptIn(ExperimentalCoilApi::class)
    fun save(url: String?, uri: Uri?) = viewModelScope.onIO {
        imageLoader.diskCache?.openSnapshot(url ?: return@onIO)?.use { snapshot ->
            snapshot.data.toFile().inputStream().use { inputStream ->
                ctx.contentResolver.openOutputStream(uri ?: return@onIO)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    fun delete(image: GeneratedImage) = viewModelScope.onIO {
        database.imagesDao().delete(image)
    }
}