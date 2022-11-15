package me.benguiman.spainstats.domain.models

interface DataSeries {
    val variables: List<VariableValue>
    val headlineVariable: VariableValue
    val dataType: DataType
}

object PercentageOfPopulationOf65OrMoreSeries : DataSeries {
    override val variables = listOf(
        PercentageOfPopulation65OrMore,
        BaseData
    )
    override val headlineVariable = PercentageOfPopulation65OrMore
    override val dataType = DataType.PERCENTAGE
}

object PercentageOfPopulationYoungerThan18Series : DataSeries {
    override val variables = listOf(
        PercentageOfPopulation18OrYounger,
        BaseData
    )
    override val headlineVariable = PercentageOfPopulation18OrYounger
    override val dataType = DataType.PERCENTAGE
}

object AverageGrossHomeIncomeSeries : DataSeries {
    override val variables = listOf(
        GrossIncomePerHome,
        BaseData
    )
    override val headlineVariable = GrossIncomePerHome
    override val dataType = DataType.MONETARY
}

object AverageGrossPersonIncomeSeries : DataSeries {
    override val variables = listOf(
        GrossIncomePerPerson,
        BaseData
    )
    override val headlineVariable = GrossIncomePerPerson
    override val dataType = DataType.MONETARY
}

object AveragePopulationAgeSeries : DataSeries {
    override val variables = listOf(
        AveragePopulationAge,
        BaseData
    )
    override val headlineVariable = AveragePopulationAge
    override val dataType = DataType.INTEGER
}

object IpvaAnnualVariation : DataSeries {
    override val variables = listOf(
        AnnualVariation,
        TotalOfHomeSize,
        TotalOfHomeTypes
    )
    override val headlineVariable = AnnualVariation
    override val dataType = DataType.DOUBLE
}