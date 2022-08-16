package me.benguiman.spainstats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.network.*
import me.benguiman.spainstats.di.IoDispatcher
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val ineService: IneService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : StatsRepository {
    override suspend fun getAdrhData(municipality: Municipality) {
        return withContext(coroutineDispatcher) {
            ineService.getDataByGeographicLocation(
                operation = AdrhOperation.KEY,
                locationIdentifier = municipality.getLocationIdentifier()
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

    private fun Municipality.getLocationIdentifier() =
        "$MUNICIPALITY_VARIABLE_KEY:${id}"
}

