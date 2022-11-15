package me.benguiman.spainstats.data

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import me.benguiman.spainstats.data.network.*
import me.benguiman.spainstats.domain.models.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

const val AVERAGE_POPULATION_SIZE_TITLE = "Edad media de la población"

class MunicipalityStatsRepositoryImplTest {


    lateinit var municipalityStatsRepositoryImpl: MunicipalityStatsRepositoryImpl


    @Before
    fun setup() {
        val ineService = MockIneService()
        municipalityStatsRepositoryImpl = MunicipalityStatsRepositoryImpl(
            ineService,
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun getDataByOperationFilterByVariable_MedianPopulationAgeSeries_nonEmpty() {
        runBlocking {
            val adrhData = municipalityStatsRepositoryImpl.getDataByOperationFilterByVariable(
                operation = AdrhOperation,
                variableId = MunicipalityVariable.ID,
                variableValue = 563,
                series = listOf(
                    PercentageOfPopulationOf65OrMoreSeries,
                    PercentageOfPopulationYoungerThan18Series,
                    AverageGrossHomeIncomeSeries,
                    AverageGrossPersonIncomeSeries,
                    AveragePopulationAgeSeries
                )
            )
            assertEquals(1, adrhData.size)
            assertEquals(AVERAGE_POPULATION_SIZE_TITLE, adrhData.iterator().next().value.name)
        }
    }

    @Test
    fun getDataByOperationFilterByVariable_nonEmpty() {
        runBlocking {
            val adrhData = municipalityStatsRepositoryImpl.getDataByOperationFilterByVariable(
                operation = AdrhOperation,
                variableId = MunicipalityVariable.ID,
                variableValue = 563,
                series = listOf(AverageGrossPersonIncomeSeries)
            )
            assertTrue(adrhData.isEmpty())
        }
    }
}

class MockIneService : IneService {
    override suspend fun getDataByOperationFilterByVariable(
        language: Language,
        operation: Int,
        locationIdentifier: String,
        type: Type,
        dataSeries: Int,
        pValue: Int
    ): List<DataEntryDto> {
        return dataResponse
    }

    override suspend fun getVariableValues(
        language: Language,
        variableId: Int,
        page: Int
    ): List<VariableValueDto> {
        throw IllegalStateException()
    }

    override suspend fun getTableData(
        language: Language,
        tableId: String,
        type: Type
    ): List<DataEntryDto> {
        throw IllegalStateException()
    }
}

private val dataResponse = listOf(
    DataEntryDto(
        name = "Barcelona. Edad media de la población. Dato base. ",
        metadata = listOf(
            MetadataDto(
                id = 563,
                variable = VariableDto(
                    id = 19,
                    name = "Municipios",
                    code = "MUN"
                ),
                name = "Barcelona",
                code = "08019"
            ),
            MetadataDto(
                id = 274520,
                variable = VariableDto(
                    id = 260,
                    name = "Conceptos Demográficos",
                    code = ""
                ),
                name = AVERAGE_POPULATION_SIZE_TITLE,
                code = ""
            ),
            MetadataDto(
                id = 72,
                variable = VariableDto(
                    id = 3,
                    name = "Tipo de dato",
                    code = ""
                ),
                name = "Dato base",
                code = ""
            )
        ),
        dataDto = listOf(
            DataDto(
                date = 1546297200000,
                year = 2019,
                value = 44.1
            )
        )
    )

)