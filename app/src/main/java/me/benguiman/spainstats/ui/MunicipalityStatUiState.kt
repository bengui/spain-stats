package me.benguiman.spainstats.ui

import me.benguiman.spainstats.MunicipalityStat

data class MunicipalityStatUiState(
    val loading: Boolean = false,
    val municipalityStatList: List<MunicipalityStat> = emptyList(),
    val errorMessage: String = ""
)