package me.benguiman.spainstats.data.network

interface DataSeries {
    val variables: List<VariableValue>
}

object PercentageOfPopulationOf65OrMoreSeries : DataSeries {
    override val variables = listOf(
        PercentageOfPopulation65OrMore,
        BaseData
    )
}

object PercentageOfPopulationYoungerThan18Series : DataSeries {
    override val variables = listOf(
        PercentageOfPopulation18OrYounger,
        BaseData
    )
}

object AverageGrossHomeIncomeSeries : DataSeries {
    override val variables = listOf(
        GrossIncomePerHome,
        BaseData
    )
}

object AverageGrossPersonIncomeSeries : DataSeries {
    override val variables = listOf(
        GrossIncomePerPerson,
        BaseData
    )
}

object AveragePopulationAgeSeries : DataSeries {
    override val variables = listOf(
        AveragePopulationAge,
        BaseData
    )
}

object IpvaAnnualVariation : DataSeries {
    override val variables = listOf(
        AnnualVariation,
        TotalOfHomeSize,
        TotalOfHomeTypes
    )
}