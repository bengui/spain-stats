package me.benguiman.spainstats.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.LocationsRepository
import me.benguiman.spainstats.data.MunicipalityStatsRepository
import me.benguiman.spainstats.di.MainDispatcher
import me.benguiman.spainstats.domain.models.*
import me.benguiman.spainstats.ui.MunicipalityStat
import me.benguiman.spainstats.ui.MunicipalityStatReport
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
            // TODO Create "report" entity to specify how to display the data.

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
                    ).map { it.value }
            )

            municipalityStats.addAll(
                municipalityStatsRepository.getTableDataByMunicipality(
                    tableData = BuildingsAndRealStateTableData,
                    municipalityCode = municipalityCode
                ).map { it.value }
            )

            municipalityStats.addAll(
                municipalityStatsRepository.getOperationDataByMunicipalityFilteredBySeries(
                    operation = IpvaOperation,
                    municipalityId = municipalityId,
                    IpvaAnnualVariation
                ).map { it.value }
            )

            MunicipalityStatReport(
                municipalityName = locationsRepository.getMunicipality(municipalityId).name,
                municipalityStatList = municipalityStats
            )
        }
    }
}