package me.benguiman.spainstats.ui.municipality

import me.benguiman.spainstats.ui.MunicipalityStat
import me.benguiman.spainstats.ui.ScreenStatus

sealed class MunicipalityError
object NoDataError : MunicipalityError()
object ResponseError : MunicipalityError()

data class MunicipalityStatUiState(
    val municipalityName : String = "",
    val municipalityStatList: List<MunicipalityStat> = emptyList(),
    val screenStatus: ScreenStatus,
    val error: MunicipalityError? = null
)