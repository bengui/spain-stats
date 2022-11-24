package me.benguiman.spainstats.ui.municipality

import me.benguiman.spainstats.ui.ReportRowUi
import me.benguiman.spainstats.ui.ScreenStatus

sealed class MunicipalityError
object NoDataError : MunicipalityError()
object ResponseError : MunicipalityError()

data class MunicipalityStatUiState(
    val municipalityName: String = "",
    val provinceName: String = "",
    val municipalityStatReportRowUiList: List<ReportRowUi> = emptyList(),
    val screenStatus: ScreenStatus,
    val error: MunicipalityError? = null
)