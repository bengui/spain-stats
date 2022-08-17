package me.benguiman.spainstats.data.network

interface Operation {
    val seriesCode: String
}

abstract class AdrhOperation : Operation {
    companion object {
        const val KEY = 353
    }
}

object PercentageOfPopulationOf65OrMore : AdrhOperation() {
    override val seriesCode: String = "ADRH7061034"
}

object PercentageOfPopulationYoungerThan18 : AdrhOperation() {
    override val seriesCode: String = "ADRH7061035"
}

object AverageGrossHomeIncome : AdrhOperation() {
    override val seriesCode: String = "ADRH9612238"
}

object AverageGrossPersonIncome : AdrhOperation() {
    override val seriesCode: String = "ADRH9612239"
}

object AveragePopulationAge : AdrhOperation() {
    override val seriesCode: String = "ADRH7218686"
}