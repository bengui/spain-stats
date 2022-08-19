package me.benguiman.spainstats.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.MunicipalityStat
import me.benguiman.spainstats.data.MunicipalityStatsRepository
import me.benguiman.spainstats.data.network.*
import me.benguiman.spainstats.di.MainDispatcher
import javax.inject.Inject

class GetDataForMunicipality @Inject constructor(
    private val municipalityStatsRepository: MunicipalityStatsRepository,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        municipalityId: Int,
        municipalityCode: String
    ): List<MunicipalityStat> {
        return withContext(coroutineDispatcher) {

            val municipalityStats = mutableListOf<MunicipalityStat>()
            municipalityStats.addAll(
                municipalityStatsRepository
                    .getOperationDataByMunicipalityFilteredBySeries(
                        operation = AdrhOperation,
                        municipalityId = municipalityId,
                        PercentageOfPopulationOf65OrMoreSeries,
                        PercentageOfPopulationYoungerThan18Series,
                        AverageGrossHomeIncomeSeries,
                        AverageGrossPersonIncomeSeries,
                        AveragePopulationAgeSeries
                    )
            )


            municipalityStats.addAll(
                municipalityStatsRepository.getOperationDataByMunicipalityFilteredBySeries(
                    operation = IpvaOperation,
                    municipalityId = municipalityId,
                    IpvaAnnualVariation
                )
            )

            municipalityStats.addAll(
                municipalityStatsRepository.getTableDataByMunicipality(
                    tableData = BuildingsAndRealState,
                    municipalityCode = municipalityCode
                )
            )

            municipalityStats
        }
    }
}