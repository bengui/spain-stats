package me.benguiman.spainstats.data

import me.benguiman.spainstats.MunicipalityStat
import me.benguiman.spainstats.data.network.DataSeries
import me.benguiman.spainstats.data.network.Operation

interface MunicipalityStatsRepository {

    suspend fun getOperationDataByMunicipalityFilteredBySeries(
        operation: Operation,
        municipalityId: Int,
        vararg series: DataSeries
    ): List<MunicipalityStat>
}