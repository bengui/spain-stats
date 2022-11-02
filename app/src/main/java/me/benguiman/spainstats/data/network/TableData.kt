package me.benguiman.spainstats.data.network

interface TableData {
    val id: String
    val headlineCodes: List<HeadlineCode>
}

data class HeadlineCode(
    val headline: String,
    val dataType: DataType
)

object BuildingsAndRealState : TableData {
    override val id: String = "t20/e244/edificios/p04/l0/1mun00.px"
    override val headlineCodes: List<HeadlineCode> = listOf(
        HeadlineCode("edificios", DataType.INTEGER),
        HeadlineCode("inmuebles", DataType.INTEGER)
    )
}