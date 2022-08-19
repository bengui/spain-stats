package me.benguiman.spainstats.data.network

interface Series {
    val variables: List<VariableValue>
}

object PercentageOfPopulationOf65OrMoreSeries : Series {
    override val variables = listOf(
        PercentageOfPopulation65OrMore,
        BaseData
    )
}

object PercentageOfPopulationYoungerThan18Series : Series {
    override val variables = listOf(
        PercentageOfPopulation18OrYounger,
        BaseData
    )
}

object AverageGrossHomeIncomeSeries : Series {
    override val variables = listOf(
        GrossIncomePerHome,
        BaseData
    )
}

object AverageGrossPersonIncomeSeries : Series {
    override val variables = listOf(
        GrossIncomePerPerson,
        BaseData
    )
}

object MedianPopulationAgeSeries : Series {
    override val variables = listOf(
        MedianPopulationAge,
        BaseData
    )
}