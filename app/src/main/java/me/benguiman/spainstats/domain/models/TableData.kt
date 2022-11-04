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