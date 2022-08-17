package me.benguiman.spainstats.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.MunicipalityStatsRepository
import me.benguiman.spainstats.di.MainDispatcher
import javax.inject.Inject

class GetAdrhForMunicipality @Inject constructor(
    private val municipalityStatsRepository: MunicipalityStatsRepository,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(id: Int) {
        return withContext(coroutineDispatcher){
            municipalityStatsRepository.getAdrhData(id)
        }
    }
}