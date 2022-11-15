package me.benguiman.spainstats.ui

import androidx.annotation.StringRes
import me.benguiman.spainstats.domain.models.DataType

data class MunicipalityStatReportUi(
    val municipalityName: String,
    val municipalityStatReportRowUiList: List<ReportRowUi>
)

data class MunicipalityStatUi(
    @StringRes val name: Int,
    val value: Double,
    val dataType: DataType
)

sealed class ReportRowUi
data class MultiElementRowUi(
    @StringRes val title: Int = -1,
    val statsList: List<MunicipalityStatUi>
) : ReportRowUi()

data class SimpleRowUi(val statUi: MunicipalityStatUi) : ReportRowUi()