package dev.leonardpark.chatgpt.di

import dev.leonardpark.chatgpt.repository.network.interceptor.AuthenticationInterceptor
import dev.leonardpark.chatgpt.repository.network.interceptor.ChangeEndpointInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authenticationInterceptor: AuthenticationInterceptor,
        changeEndpointInterceptor: ChangeEndpointInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(authenticationInterceptor)
        .addInterceptor(changeEndpointInterceptor)
        .readTimeout(2, TimeUnit.MINUTES)
        .build()
}