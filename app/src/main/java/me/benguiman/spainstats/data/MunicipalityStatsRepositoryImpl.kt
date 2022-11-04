package me.benguiman.spainstats.data

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.network.DataEntryDto
import me.benguiman.spainstats.data.network.IneService
import me.benguiman.spainstats.di.IoDispatcher
import me.benguiman.spainstats.domain.models.*
import me.benguiman.spainstats.ui.MunicipalityStat
import javax.inject.Inject

class MunicipalityStatsRepositoryImpl @Inject constructor(
    private val ineService: IneService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : MunicipalityStatsRepository {

    override suspend fun getOperationDataByMunicipalityFilteredBySeries(
        operation: Operation,
        municipalityId: Int,
        vararg series: DataSeries
    ): Map<DataSeries, MunicipalityStat> {
        return getDataByOperationFilterByVariable(
            operation = operation,
            variableId = MunicipalityVariable.ID,
            variableValue = municipalityId,
            series = series.toList()
        )
    }

    override suspend fun getTableDataByMunicipality(
        tableData: TableData,
        municipalityCode: String
    ): Map<HeadlineCode, MunicipalityStat> {
        return withContext(coroutineDispatcher) {
            ineService.getTableData(tableId = tableData.id)
                .filterNameByMunicipalityId(municipalityCode)
                .mapToMunicipalityStatWithHeadlineCode(tableData.headlineCodes)
        }
    }

    @VisibleForTesting
    suspend fun getDataByOperationFilterByVariable(
        operation: Operation,
        variableId: Int,
        variableValue: Int,
        series: List<DataSeries>
    ): Map<DataSeries, MunicipalityStat> {
        return withContext(coroutineDispatcher) {
            val variableValueSet =
                mutableSetOf<VariableValue>(GenericVariable(variableId, variableValue))
            variableValueSet.addAll(series.toVariableList())

            ineService.getDataByOperationFilterByVariable(
                operation = operation.key,
                locationIdentifier = formatVariableValue(variableId, variableValue)
            )
                .filterEmptyData()
                .filterByVariables(variableValueSet)
                .mapToDataEntryDtoWithDataSeries(series)
                .mapDataSeriesDataEntryDtoToMunicipalityStat()
        }
    }

    private fun formatVariableValue(variableId: Int, variableValueId: Int) =
        "$variableId:$variableValueId"

    private fun List<DataEntryDto>.filterByVariables(
        variableValueSet: Set<VariableValue>
    ): List<DataEntryDto> =
        this.filter { dataEntry ->
            dataEntry.metadata.all { variableValueDto ->
                variableValueSet.any { seriesVariable ->
                    seriesVariable.variableId == variableValueDto.variable.id
                            && seriesVariable.value == variableValueDto.id
                }
            }
        }

    private fun List<DataEntryDto>.filterNameByMunicipalityId(municipalityCode: String): List<DataEntryDto> =
        this.filter { dataEntryDto ->
            dataEntryDto.metadata.any { variableValueDto ->
                variableValueDto.variable.id == MunicipalityVariable.ID
                        && variableValueDto.code == municipalityCode
            }
        }

    private fun List<DataSeries>.toVariableList(): Set<VariableValue> =
        this.fold(mutableSetOf()) { list, item ->
            list.addAll(item.variables)
            list
        }

    private fun List<DataEntryDto>.mapToMunicipalityStatWithHeadlineCode(codeList: List<HeadlineCode>): Map<HeadlineCode, MunicipalityStat> =
        this.associate {
            val headlineCode = codeList.first { headlineCode ->
                it.metadata.any { metadataDto -> headlineCode.headline == metadataDto.code }
            }
            headlineCode to
            MunicipalityStat(
                name = headlineCode.headline,
                value = it.dataDto.first().value
                    ?: throw IllegalStateException("data should not be empty"),
                dataType = headlineCode.dataType
            )
        }

    private fun List<DataEntryDto>.mapToDataEntryDtoWithDataSeries(series: List<DataSeries>): List<Pair<DataSeries, DataEntryDto>> =
        this.map { dataEntryDto ->
            series.first { dataSeries ->
                dataSeries.variables.all { variableValue ->
                    dataEntryDto.metadata.any { metadataDto ->
                        variableValue.value == metadataDto.id
                                && variableValue.variableId == metadataDto.variable.id
                    }
                }
            } to dataEntryDto
        }

    private fun List<Pair<DataSeries, DataEntryDto>>.mapDataSeriesDataEntryDtoToMunicipalityStat(): Map<DataSeries, MunicipalityStat> =
        this.associate {
            it.first to
                    MunicipalityStat(
                        name = it.second.metadata.first { metadataDto ->
                            it.first.headlineVariable.value == metadataDto.id
                                    && it.first.headlineVariable.variableId == metadataDto.variable.id
                        }.name,
                        value = it.second.dataDto.first().value
                            ?: throw IllegalStateException("data should not be empty"),
                        dataType = it.first.dataType
                    )
        }

    private fun List<DataEntryDto>.filterEmptyData() =
        this.filter {
            it.dataDto.isNotEmpty() &&
                    it.dataDto.all { dataDto -> dataDto.value != null }
        }
}


