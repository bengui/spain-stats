package me.benguiman.spainstats.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.benguiman.spainstats.data.network.CacheNetworkInterceptor
import me.benguiman.spainstats.data.network.IneService
import me.benguiman.spainstats.data.network.ReTryInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideIneService(
        @ApplicationContext context: Context,
        reTryInterceptor: ReTryInterceptor,
        cacheNetworkInterceptor: CacheNetworkInterceptor
    ): IneService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val maxSize: Long = 50 * 1024 * 1024
        val cache = Cache(context.cacheDir, maxSize)
        val client: OkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(cacheNetworkInterceptor)
            .addInterceptor(reTryInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://servicios.ine.es/wstempus/js/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(IneService::class.java)
    }
}

