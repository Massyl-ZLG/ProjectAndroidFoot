package com.example.myapplication.di


import com.example.myapplication.model.data.SoccerApi
import com.example.myapplication.model.data.SoccerApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class SoccerApiProvider {
    @Provides
    @Singleton
    fun provideAuthInterceptorOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {


        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideSoccerApiService(retrofit: Retrofit): SoccerApi.Service {
        return retrofit.create(SoccerApi.Service::class.java)
    }

}