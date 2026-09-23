package com.mountplanner.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// This class is complementary to NetworkModule, useful if a builder pattern is needed directly outside of DI.
object ApiClient {

    fun createOkHttpClient(isDebug: Boolean): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (isDebug) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    inline fun <reified T> createService(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(T::class.java)
    }
}
