package me.benguiman.spainstats.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.benguiman.spainstats.data.LocationsRepository
import me.benguiman.spainstats.data.LocationsRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AppBinderModule {

    @Singleton
    @Binds
    abstract fun bindLocationsRepository(locationsRepositoryImpl: LocationsRepositoryImpl): LocationsRepository
}