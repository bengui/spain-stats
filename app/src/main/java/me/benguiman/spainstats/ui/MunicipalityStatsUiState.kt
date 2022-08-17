package me.benguiman.spainstats.ui

data class MunicipalityStatsUiState(
    val provinceMunicipalityList: List<ProvinceMunicipalityListItemUiState> = emptyList(),
    val errorMessage: String = ""
)

data class ProvinceMunicipalityListItemUiState(
    val name: String = "",
    val title: Boolean = false
)