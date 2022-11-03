package me.benguiman.spainstats.ui.home

data class MunicipalityHomeUiState(
    val provinceMunicipalityList: List<ProvinceMunicipalityListItem> = emptyList(),
    val municipalityList: List<MunicipalityUiState> = emptyList(),
    val homeScreenStatus : HomeScreenStatus
)

sealed class HomeScreenStatus
object HomeScreenError : HomeScreenStatus()
object HomeScreenLoading : HomeScreenStatus()
object HomeScreenSuccess : HomeScreenStatus()

data class ProvinceMunicipalityListItem(
    val id: Int = -1,
    val name: String = "",
    val code: String = "",
    val title: Boolean = false
)

data class MunicipalityUiState(
    val id: Int,
    val name: String,
    val code: String
)

data class ListItem(
    val name: String = "",
    val title: Boolean = false
)

data class MunicipalityStatsUiState(
    val provinceMunicipalityList: List<ListItem> = emptyList(),
    val errorMessage: String = ""
)
