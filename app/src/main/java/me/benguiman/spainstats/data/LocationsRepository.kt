package me.benguiman.spainstats.data

interface LocationsRepository {

    suspend fun getAutonomousCommunityList(): List<AutonomousCommunity>

    suspend fun getProvinceListPopulatedWithMunicipalities(): List<Province>

    suspend fun getMunicipality(municipalityId : Int) : Municipality
}

data class Municipality(
    val id: Int,
    val name: String,
    val code: String
)

data class Province(
    val id: Int,
    val name: String,
    val code: String,
    val municipalityList: List<Municipality>
)

data class AutonomousCommunity(
    val id: Int,
    val name: String,
    val code: String
)

const val MUNICIPALITY_VARIABLE_KEY = 19
const val PROVINCE_VARIABLE_KEY = 115
const val AUTONOMOUS_COMMUNITY_VARIABLE_KEY = 70
