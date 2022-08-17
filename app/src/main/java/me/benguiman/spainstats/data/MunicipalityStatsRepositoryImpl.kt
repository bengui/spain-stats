package me.benguiman.spainstats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.network.*
import me.benguiman.spainstats.di.IoDispatcher
import javax.inject.Inject

class MunicipalityStatsRepositoryImpl @Inject constructor(
    private val ineService: IneService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : MunicipalityStatsRepository {
    override suspend fun getAdrhData(municipalityId: Int) {
        return withContext(coroutineDispatcher) {
            ineService.getDataByGeographicLocation(
                operation = AdrhOperation.KEY,
                locationIdentifier = getLocationIdentifier(municipalityId)
            ).filterByOperation(
                PercentageOfPopulationOf65OrMore,
                PercentageOfPopulationYoungerThan18,
                AverageGrossHomeIncome,
                AverageGrossPersonIncome,
                AveragePopulationAge
            )
        }
    }

    private fun List<DataEntryDto>.filterByOperation(
        vararg operations: Operation
    ): List<DataEntryDto> {
        val operationSet = operations.map { it.seriesCode }.toSet()
        return filter {
            operationSet.contains(it.code)
        }
    }

    private fun getLocationIdentifier(id : Int) =
        "$MUNICIPALITY_VARIABLE_KEY:${id}"
}

