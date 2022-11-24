package me.benguiman.spainstats.ui

import androidx.annotation.StringRes
import me.benguiman.spainstats.domain.models.DataType

data class MunicipalityStatReportUi(
    val municipalityName: String,
    val provinceName: String,
    val municipalityStatReportRowUiList: List<ReportRowUi>
)

data class MunicipalityStatUi(
    @StringRes val name: Int,
    val value: Double,
    val dataType: DataType
)

sealed class ReportRowUi(open val id: Int) {
    companion object {
        const val POPULATION_ROW = 100
        const val AVERAGE_INCOME_ROW = 101
        const val BUILDINGS_COUNT_ROW = 102
        const val ESTATE_COUNT_ROW = 103
        const val IPVA_ROW = 104
    }
}
data class MultiElementRowUi(
    @StringRes val title: Int = -1,
    val statsList: List<MunicipalityStatUi>,
    override val id: Int = -1
) : ReportRowUi(id)

data class SimpleRowUi(
    val statUi: MunicipalityStatUi,
    override val id: Int = -1
) : ReportRowUi(id)