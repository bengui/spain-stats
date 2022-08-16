package me.benguiman.spainstats.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.benguiman.spainstats.data.network.IneService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideIneService(): IneService =
        Retrofit.Builder()
            .baseUrl("https://servicios.ine.es/wstempus/js/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(IneService::class.java)


}