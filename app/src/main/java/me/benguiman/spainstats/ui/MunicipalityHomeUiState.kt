package me.benguiman.spainstats.ui

data class MunicipalityHomeUiState(
    val provinceMunicipalityList: List<ProvinceMunicipalityListItem> = emptyList(),
    val errorMessage: String = ""
)

data class ProvinceMunicipalityListItem(
    val id : Int = -1,
    val name: String = "",
    val title: Boolean = false
)

data class ListItem(
    val name: String = "",
    val title: Boolean = false
)

data class MunicipalityStatsUiState(
    val provinceMunicipalityList: List<ListItem> = emptyList(),
    val errorMessage: String = ""
)
