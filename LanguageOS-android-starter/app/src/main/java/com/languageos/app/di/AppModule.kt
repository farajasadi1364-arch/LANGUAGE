package com.languageos.app.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.languageos.app.data.local.AppDatabase
import com.languageos.app.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// TODO: بعد از بالا آمدن بک‌اند طبق بخش ۱۱ PRD، این آدرس رو با API واقعی جایگزین کن.
// تا اون موقع می‌تونی یک Mock Server (مثلاً json-server) با همون مسیرها بالا بیاری.
private const val BASE_URL = "https://api.languageos.example.com/v1/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "languageos.db")
            .fallbackToDestructiveMigration() // فقط برای مرحله توسعه؛ قبل از انتشار واقعی حذف شود
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
