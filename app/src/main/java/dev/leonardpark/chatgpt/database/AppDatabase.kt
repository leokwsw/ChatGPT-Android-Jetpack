package dev.leonardpark.chatgpt.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import dev.leonardpark.chatgpt.database.dao.ChatsDao
import dev.leonardpark.chatgpt.database.dao.GeneratedAudiosDao
import dev.leonardpark.chatgpt.database.dao.GeneratedImagesDao
import dev.leonardpark.chatgpt.database.dao.MessagesDao
import dev.leonardpark.chatgpt.database.entity.Chat
import dev.leonardpark.chatgpt.database.entity.ChatMessage
import dev.leonardpark.chatgpt.database.entity.GeneratedAudio
import dev.leonardpark.chatgpt.database.entity.GeneratedImage

@Database(entities = [Chat::class, ChatMessage::class, GeneratedImage::class, GeneratedAudio::class], version = 4)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        val MIGRATION_1_2 = Migration(1, 2) {
            it.execSQL("ALTER TABLE ChatMessage ADD COLUMN generating INTEGER NOT NULL DEFAULT 0")
        }
        val MIGRATION_2_3 = Migration(2, 3) {
            it.execSQL("CREATE TABLE GeneratedImage(id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL, prompt TEXT, url TEXT)")
        }
        val MIGRATION_3_4 = Migration(3, 4) {
            it.execSQL("CREATE TABLE GeneratedAudio(id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL, input TEXT NOT NULL, file_path TEXT NOT NULL, file_mime_type TEXT NOT NULL)")
        }
    }
    abstract fun chatsDao(): ChatsDao
    abstract fun messagesDao(): MessagesDao
    abstract fun imagesDao(): GeneratedImagesDao
    abstract fun audiosDao(): GeneratedAudiosDao
}