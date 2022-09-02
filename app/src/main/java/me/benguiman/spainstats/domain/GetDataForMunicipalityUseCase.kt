package me.benguiman.spainstats.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.MunicipalityStat
import me.benguiman.spainstats.MunicipalityStatReport
import me.benguiman.spainstats.data.LocationsRepository
import me.benguiman.spainstats.data.MunicipalityStatsRepository
import me.benguiman.spainstats.data.network.*
import me.benguiman.spainstats.di.MainDispatcher
import javax.inject.Inject

class GetDataForMunicipalityUseCase @Inject constructor(
    private val municipalityStatsRepository: MunicipalityStatsRepository,
    private val locationsRepository: LocationsRepository,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        municipalityId: Int,
        municipalityCode: String
    ): MunicipalityStatReport {
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
                municipalityStatsRepository.getTableDataByMunicipality(
                    tableData = BuildingsAndRealState,
                    municipalityCode = municipalityCode
                )
            )

            municipalityStats.addAll(
                municipalityStatsRepository.getOperationDataByMunicipalityFilteredBySeries(
                    operation = IpvaOperation,
                    municipalityId = municipalityId,
                    IpvaAnnualVariation
                )
            )

            MunicipalityStatReport(
                municipalityName = locationsRepository.getMunicipality(municipalityId).name,
                municipalityStatList = municipalityStats
            )
        }
    }
}