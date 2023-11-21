package dev.leonardpark.chatgpt

import android.app.Application
import dev.leonardpark.chatgpt.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ChatGPTApplication: Application() {

    companion object {
        const val API_ENDPOINT = "api.openai.com/v1"

        val applicationScope = MainScope()
    }

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch(Dispatchers.IO) {
            database.messagesDao().apply {
                markAllAsNotGenerating()
                deleteEmptyMessages()
            } // maybe some geniuses will remove app from recents
        }
    }
}