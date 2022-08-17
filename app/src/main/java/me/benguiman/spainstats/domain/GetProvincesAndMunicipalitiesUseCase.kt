package me.benguiman.spainstats.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.LocationsRepository
import me.benguiman.spainstats.data.Province
import me.benguiman.spainstats.di.MainDispatcher
import javax.inject.Inject

class GetProvincesAndMunicipalitiesUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): List<Province> {
        return withContext(coroutineDispatcher) {
            locationsRepository.getProvinceListPopulatedWithMunicipalities()
        }
    }
}