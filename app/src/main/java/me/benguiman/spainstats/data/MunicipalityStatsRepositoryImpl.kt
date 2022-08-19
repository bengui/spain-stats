package me.benguiman.spainstats.data

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.MunicipalityStat
import me.benguiman.spainstats.data.network.*
import me.benguiman.spainstats.data.network.Series
import me.benguiman.spainstats.di.IoDispatcher
import javax.inject.Inject

class MunicipalityStatsRepositoryImpl @Inject constructor(
    private val ineService: IneService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : MunicipalityStatsRepository {

    override suspend fun getAdrhDataByMunicipality(municipalityId: Int): List<MunicipalityStat> {
        return getDataByOperationFilterByVariable(
            operation = AdrhOperation,
            variableId = MunicipalityVariable.ID,
            variableValue = municipalityId,
            PercentageOfPopulationOf65OrMoreSeries,
            PercentageOfPopulationYoungerThan18Series,
            AverageGrossHomeIncomeSeries,
            AverageGrossPersonIncomeSeries,
            MedianPopulationAgeSeries
        )
    }

    @VisibleForTesting
    suspend fun getDataByOperationFilterByVariable(
        operation: Operation,
        variableId: Int,
        variableValue: Int,
        vararg series: Series
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
                .mapToMunicipalityStat()
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

    private fun Array<out Series>.toVariableList(): Set<VariableValue> =
        this.fold(mutableSetOf()) { list, item ->
            list.addAll(item.variables)
            list
        }

    private fun List<DataEntryDto>.mapToMunicipalityStat() =
        this.map {
            MunicipalityStat(
                name = it.name,
                value = it.dataDto.first().value
                    ?: throw IllegalStateException("data should not be empty")
            )
        }

    private fun List<DataEntryDto>.filterEmptyData() =
        this.filter {
            it.dataDto.isNotEmpty() &&
                    it.dataDto.all { dataDto -> dataDto.value != null }
        }
}


