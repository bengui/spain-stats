package me.benguiman.spainstats.ui.home

import me.benguiman.spainstats.ui.ScreenStatus

data class MunicipalityHomeUiState(
    val provinceMunicipalityList: List<ProvinceMunicipalityListItem> = emptyList(),
    val municipalityList: List<MunicipalityUiState> = emptyList(),
    val screenStatus: ScreenStatus
)

data class ProvinceMunicipalityListItem(
    val id: Int = -1,
    val name: String = "",
    val code: String = "",
    val title: Boolean = false
)

data class MunicipalityUiState(
    val id: Int,
    val name: String,
    val code: String,
    val provinceName: String
)

data class ListItem(
    val name: String = "",
    val title: Boolean = false
)

data class MunicipalityStatsUiState(
    val provinceMunicipalityList: List<ListItem> = emptyList(),
    val errorMessage: String = ""
)
