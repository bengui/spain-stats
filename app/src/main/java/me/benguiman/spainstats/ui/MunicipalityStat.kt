package me.benguiman.spainstats.ui

import me.benguiman.spainstats.domain.models.DataType

data class MunicipalityStatReport(
    val municipalityName: String,
    val municipalityStatList: List<MunicipalityStat>
)

data class MunicipalityStat(
    val name: String,
    val value: Double,
    val dataType: DataType
)
