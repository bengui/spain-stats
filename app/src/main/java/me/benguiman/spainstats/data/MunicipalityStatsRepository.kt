package me.benguiman.spainstats.data

interface MunicipalityStatsRepository {

    suspend fun getAdrhData(municipalityId: Int)
}