package me.benguiman.spainstats.data

import me.benguiman.spainstats.MunicipalityStat

interface MunicipalityStatsRepository {

    suspend fun getAdrhData(municipalityId: Int) : List<MunicipalityStat>
}