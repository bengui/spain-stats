package me.benguiman.spainstats.data

import me.benguiman.spainstats.ui.MunicipalityStat
import me.benguiman.spainstats.data.network.DataSeries
import me.benguiman.spainstats.data.network.Operation
import me.benguiman.spainstats.data.network.TableData

interface MunicipalityStatsRepository {

    suspend fun getOperationDataByMunicipalityFilteredBySeries(
        operation: Operation,
        municipalityId: Int,
        vararg series: DataSeries
    ): List<MunicipalityStat>

    suspend fun getTableDataByMunicipality(
        tableData: TableData,
        municipalityCode: String
    ): List<MunicipalityStat>
}