package me.benguiman.spainstats.data

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.MunicipalityStat
import me.benguiman.spainstats.data.network.*
import me.benguiman.spainstats.di.IoDispatcher
import javax.inject.Inject

class MunicipalityStatsRepositoryImpl @Inject constructor(
    private val ineService: IneService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : MunicipalityStatsRepository {

    override suspend fun getOperationDataByMunicipalityFilteredBySeries(
        operation: Operation,
        municipalityId: Int,
        vararg series: DataSeries
    ): List<MunicipalityStat> {
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
    ): List<MunicipalityStat> {
        return withContext(coroutineDispatcher) {
            ineService.getTableData(tableId = tableData.id)
                .filterNameByMunicipalityId(municipalityCode)
                .mapToMunicipalityStat()
        }
    }

    @VisibleForTesting
    suspend fun getDataByOperationFilterByVariable(
        operation: Operation,
        variableId: Int,
        variableValue: Int,
        series: List<DataSeries>
    ): List<MunicipalityStat> {
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

    private fun List<DataEntryDto>.filterNameByMunicipalityId(municipalityCode: String) : List<DataEntryDto> =
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

    private fun List<DataEntryDto>.mapToMunicipalityStat(): List<MunicipalityStat> =
        this.map {
            MunicipalityStat(
                name = it.name,
                value = it.dataDto.first().value
                    ?: throw IllegalStateException("data should not be empty")
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

    private fun List<Pair<DataSeries, DataEntryDto>>.mapDataSeriesDataEntryDtoToMunicipalityStat(): List<MunicipalityStat> =
        this.map {
            MunicipalityStat(
                name = it.second.metadata.first { metadataDto ->
                    it.first.headlineVariable.value == metadataDto.id
                            && it.first.headlineVariable.variableId == metadataDto.variable.id
                }.name,
                value = it.second.dataDto.first().value
                    ?: throw IllegalStateException("data should not be empty")
            )
        }

    private fun List<DataEntryDto>.filterEmptyData() =
        this.filter {
            it.dataDto.isNotEmpty() &&
                    it.dataDto.all { dataDto -> dataDto.value != null }
        }
}


