package me.benguiman.spainstats.data

data class MunicipalityStatsReport(
    val municipalityName: String,
    val series: List<Series>
)

data class Series(
    val title: String,
    val statData: List<SeriesData>
)

data class SeriesData(
    val value: Double
)