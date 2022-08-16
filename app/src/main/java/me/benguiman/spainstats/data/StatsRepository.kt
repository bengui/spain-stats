package me.benguiman.spainstats.data

interface StatsRepository {

    suspend fun getAdrhData(municipality: Municipality)
}