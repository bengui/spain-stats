package me.benguiman.spainstats.domain.models

interface TableData {
    val id: String
    val headlineCodes: List<HeadlineCode>
}

interface HeadlineCode {
    val headline: String
    val dataType: DataType
}

object BuildingsHeadlineCode : HeadlineCode {
    override val headline = "edificios"
    override val dataType = DataType.INTEGER
}

object EstateHeadlineCode : HeadlineCode {
    override val headline = "inmuebles"
    override val dataType = DataType.INTEGER
}


object BuildingsAndRealStateTableData : TableData {
    override val id: String = "t20/e244/edificios/p04/l0/1mun00.px"
    override val headlineCodes: List<HeadlineCode> = listOf(
        BuildingsHeadlineCode,
        EstateHeadlineCode
    )
}

val provinceIdPopulationTableIdMap = mapOf(
    3 to "2855",
    4 to "2856",
    5 to "2857",
    2 to "2854",
    33 to "2886",
    6 to "2858",
    7 to "2859",
    8 to "2860",
    9 to "2861",
    48 to "2905",
    10 to "2862",
    11 to "2863",
    12 to "2864",
    39 to "2893",
    13 to "2865",
    14 to "2866",
    15 to "2901",
    16 to "2868",
    17 to "2869",
    21 to "2873",
    18 to "2870",
    19 to "2871",
    20 to "2872",
    22 to "2874",
    23 to "2875",
    24 to "2876",
    25 to "2877",
    26 to "2878",
    28 to "2880",
    29 to "2881",
    30 to "2882",
    31 to "2883",
    32 to "2884",
    53 to "2885",
    34 to "2888",
    35 to "2889",
    36 to "2890",
    27 to "2879",
    37 to "2891",
    38 to "2892",
    40 to "2894",
    41 to "2895",
    42 to "2896",
    43 to "2900",
    44 to "2899",
    45 to "2902",
    46 to "2903",
    47 to "2904",
    49 to "2906",
    50 to "2907",
    51 to "2908",
    52 to "2909"
)