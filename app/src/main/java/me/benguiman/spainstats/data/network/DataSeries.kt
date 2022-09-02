package me.benguiman.spainstats.data.network

interface DataSeries {
    val variables: List<VariableValue>
    val headlineVariable: VariableValue
}

object PercentageOfPopulationOf65OrMoreSeries : DataSeries {
    override val variables = listOf(
        PercentageOfPopulation65OrMore,
        BaseData
    )
    override val headlineVariable = PercentageOfPopulation65OrMore
}

object PercentageOfPopulationYoungerThan18Series : DataSeries {
    override val variables = listOf(
        PercentageOfPopulation18OrYounger,
        BaseData
    )
    override val headlineVariable = PercentageOfPopulation18OrYounger
}

object AverageGrossHomeIncomeSeries : DataSeries {
    override val variables = listOf(
        GrossIncomePerHome,
        BaseData
    )
    override val headlineVariable = GrossIncomePerHome
}

object AverageGrossPersonIncomeSeries : DataSeries {
    override val variables = listOf(
        GrossIncomePerPerson,
        BaseData
    )
    override val headlineVariable = GrossIncomePerPerson
}

object AveragePopulationAgeSeries : DataSeries {
    override val variables = listOf(
        AveragePopulationAge,
        BaseData
    )
    override val headlineVariable = AveragePopulationAge
}

object IpvaAnnualVariation : DataSeries {
    override val variables = listOf(
        AnnualVariation,
        TotalOfHomeSize,
        TotalOfHomeTypes
    )
    override val headlineVariable = TotalOfHomeSize
}