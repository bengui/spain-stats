package me.benguiman.spainstats.data.network

interface TableData {
    val id: String
    val headlineCodes: List<String>
}

object BuildingsAndRealState : TableData {
    override val id: String = "t20/e244/edificios/p04/l0/1mun00.px"
    override val headlineCodes: List<String> = listOf("edificios", "inmuebles")
}