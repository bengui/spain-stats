package me.benguiman.spainstats

data class MunicipalityStatReport(
    val municipalityName: String,
    val municipalityStatList: List<MunicipalityStat>
)

data class MunicipalityStat(
    val name: String,
    val value: Double
)
