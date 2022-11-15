package me.benguiman.spainstats.data

import me.benguiman.spainstats.domain.models.DataSeries
import me.benguiman.spainstats.domain.models.HeadlineCode
import me.benguiman.spainstats.domain.models.Operation
import me.benguiman.spainstats.domain.models.TableData
import me.benguiman.spainstats.domain.models.MunicipalityStat

interface MunicipalityStatsRepository {

    suspend fun getOperationDataByMunicipalityFilteredBySeries(
        operation: Operation,
        municipalityId: Int,
        vararg series: DataSeries
    ): Map<DataSeries, MunicipalityStat>

    suspend fun getTableDataByMunicipality(
        tableData: TableData,
        municipalityCode: String
    ): Map<HeadlineCode, MunicipalityStat>
}