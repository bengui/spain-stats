package me.benguiman.spainstats.domain

import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.R
import me.benguiman.spainstats.data.LocationsRepository
import me.benguiman.spainstats.data.MunicipalityStatsRepository
import me.benguiman.spainstats.di.MainDispatcher
import me.benguiman.spainstats.domain.models.*
import me.benguiman.spainstats.domain.models.MunicipalityStat
import me.benguiman.spainstats.ui.*
import javax.inject.Inject

class GetDataForMunicipalityUseCase @Inject constructor(
    private val municipalityStatsRepository: MunicipalityStatsRepository,
    private val locationsRepository: LocationsRepository,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        municipalityId: Int,
        municipalityCode: String
    ): MunicipalityStatReportUi {
        return withContext(coroutineDispatcher) {

            //TODO Obtain the population from the municipality

            val populationMunicipalityStatUi = mutableListOf<MunicipalityStatUi>()
            val incomeMunicipalityStatUi = mutableListOf<MunicipalityStatUi>()
            val estateDataMunicipalityStatUi = mutableListOf<MunicipalityStatUi>()
            val ipvaMunicipalityStats = mutableListOf<MunicipalityStatUi>()

            municipalityStatsRepository
                .getOperationDataByMunicipalityFilteredBySeries(
                    operation = AdrhOperation,
                    municipalityId = municipalityId,
                    PercentageOfPopulationOf65OrMoreSeries,
                    PercentageOfPopulationYoungerThan18Series,
                    AverageGrossHomeIncomeSeries,
                    AverageGrossPersonIncomeSeries,
                    AveragePopulationAgeSeries
                ).also { map ->
                    with(populationMunicipalityStatUi) {
                        addIfPresent(
                            PercentageOfPopulationOf65OrMoreSeries,
                            R.string.percentage_of_population_65_or_more,
                            map
                        )
                        addIfPresent(
                            PercentageOfPopulationYoungerThan18Series,
                            R.string.percentage_of_population_younger_than_18,
                            map
                        )
                        addIfPresent(
                            AveragePopulationAgeSeries,
                            R.string.average_population_age,
                            map
                        )
                    }

                    with(incomeMunicipalityStatUi) {
                        addIfPresent(
                            AverageGrossPersonIncomeSeries,
                            R.string.gross_income_per_person,
                            map
                        )
                        addIfPresent(
                            AverageGrossHomeIncomeSeries,
                            R.string.gross_income_per_home,
                            map
                        )
                    }
                }


            municipalityStatsRepository.getTableDataByMunicipality(
                tableData = BuildingsAndRealStateTableData,
                municipalityCode = municipalityCode
            ).also { map ->
                estateDataMunicipalityStatUi.addIfPresent(
                    EstateHeadlineCode,
                    R.string.estate_count,
                    map
                )
                estateDataMunicipalityStatUi.addIfPresent(
                    BuildingsHeadlineCode,
                    R.string.buildings_count,
                    map
                )
            }

            municipalityStatsRepository.getOperationDataByMunicipalityFilteredBySeries(
                operation = IpvaOperation,
                municipalityId = municipalityId,
                IpvaAnnualVariation
            ).also { map ->
                ipvaMunicipalityStats.addIfPresent(
                    IpvaAnnualVariation,
                    R.string.ipva_estate_annual_variation,
                    map
                )
            }

            MunicipalityStatReportUi(
                municipalityName = locationsRepository.getMunicipality(municipalityId).name,
                municipalityStatReportRowUiList = generateReportRows(
                    populationMunicipalityStatUi,
                    incomeMunicipalityStatUi,
                    estateDataMunicipalityStatUi,
                    ipvaMunicipalityStats
                )
            )
        }
    }

    private fun generateReportRows(
        populationMunicipalityStatUi: MutableList<MunicipalityStatUi>,
        incomeMunicipalityStatUi: MutableList<MunicipalityStatUi>,
        estateDataMunicipalityStatUi: MutableList<MunicipalityStatUi>,
        ipvaMunicipalityStats: MutableList<MunicipalityStatUi>
    ): MutableList<ReportRowUi> {
        val rowList = mutableListOf<ReportRowUi>()

        if (populationMunicipalityStatUi.isNotEmpty()) {
            rowList.add(
                MultiElementRowUi(
                    title = R.string.population_title,
                    statsList = populationMunicipalityStatUi,
                    id = ReportRowUi.POPULATION_ROW
                )
            )
        }

        if (incomeMunicipalityStatUi.isNotEmpty()) {
            rowList.add(
                MultiElementRowUi(
                    title = R.string.average_gross_income_title,
                    statsList = incomeMunicipalityStatUi,
                    id = ReportRowUi.AVERAGE_INCOME_ROW
                )
            )
        }

        estateDataMunicipalityStatUi.forEach {
            rowList.add(SimpleRowUi(it))
        }

        ipvaMunicipalityStats.forEach {
            rowList.add(SimpleRowUi(it))
        }

        return rowList
    }


    private fun MutableList<MunicipalityStatUi>.addIfPresent(
        dataSeries: DataSeries,
        @StringRes name: Int,
        map: Map<DataSeries, MunicipalityStat>
    ) {
        map[dataSeries]?.let {
            this.add(
                MunicipalityStatUi(
                    name = name,
                    value = it.value,
                    dataType = dataSeries.dataType
                )
            )
        }
    }

    private fun MutableList<MunicipalityStatUi>.addIfPresent(
        headlineCode: HeadlineCode,
        @StringRes name: Int,
        map: Map<HeadlineCode, MunicipalityStat>
    ) {
        map[headlineCode]?.let {
            this.add(
                MunicipalityStatUi(
                    name = name,
                    value = it.value,
                    dataType = headlineCode.dataType
                )
            )
        }
    }
}