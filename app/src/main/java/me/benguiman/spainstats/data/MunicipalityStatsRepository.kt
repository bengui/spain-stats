package me.benguiman.spainstats.data

import me.benguiman.spainstats.MunicipalityStat

interface MunicipalityStatsRepository {

    suspend fun getAdrhDataByMunicipality(municipalityId: Int): List<MunicipalityStat>
}