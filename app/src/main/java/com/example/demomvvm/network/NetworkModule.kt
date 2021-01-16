package com.example.demomvvm.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
            .connectTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .addInterceptor(SupportInterceptor())
            .authenticator(SupportInterceptor())
        return builder.build()
    }

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return "https://reqres.in"
    }
    @Singleton
    @Provides
    fun retrofitClient(client: OkHttpClient, @Named("baseUrl")baseUrl:String): Retrofit {
        return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
    }

    @Singleton
    @Provides
    fun getAPiclicent(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }



}