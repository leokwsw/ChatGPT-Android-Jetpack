package dev.leonardpark.chatgpt.di

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder()
        .addSerializationExclusionStrategy(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes) = f.getAnnotation(GsonSkip::class.java) != null
            override fun shouldSkipClass(clazz: Class<*>?) = false
        })
        .create()
}

@Target(AnnotationTarget.FIELD)
annotation class GsonSkip