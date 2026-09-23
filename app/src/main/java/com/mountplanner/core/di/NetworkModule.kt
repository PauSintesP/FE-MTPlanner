package com.mountplanner.core.di

import com.mountplanner.BuildConfig
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.data.remote.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @Named("MountReporter")
    fun provideMountReporterRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            // Assume BuildConfig.MOUNT_REPORTER_BASE_URL exists
            .baseUrl("https://api.mountreporter.example.com/") 
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("Weather")
    fun provideWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMountReporterApiService(@Named("MountReporter") retrofit: Retrofit): MountReporterApiService {
        return retrofit.create(MountReporterApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(@Named("Weather") retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }
}
