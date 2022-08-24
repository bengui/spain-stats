package me.benguiman.spainstats.ui.municipality

import me.benguiman.spainstats.MunicipalityStat

sealed class MunicipalityError
object NoDataError : MunicipalityError()
object ResponseError : MunicipalityError()

data class MunicipalityStatUiState(
    val loading: Boolean = false,
    val municipalityName : String = "",
    val municipalityStatList: List<MunicipalityStat> = emptyList(),
    val error: MunicipalityError? = null
)