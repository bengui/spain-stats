package me.benguiman.spainstats.data.network

interface VariableValue {
    val variableId: Int
    val value: Int
}

class GenericVariable(
    override val variableId: Int,
    override val value: Int
) : VariableValue

// Variables

abstract class MunicipalityVariable : VariableValue {
    override val variableId: Int = ID

    companion object {
        const val ID = 19
    }
}

class MunicipalityValue(override val value: Int) : MunicipalityVariable()

abstract class DemographicConceptsVariable : VariableValue {
    override val variableId: Int = ID

    companion object {
        const val ID = 260
    }
}

abstract class DataTypeVariable : VariableValue {
    override val variableId: Int = ID

    companion object {
        const val ID = 3
    }
}

abstract class AccountingBalances : VariableValue {
    override val variableId: Int = ID

    companion object {
        const val ID = 482
    }
}

abstract class HomeType : VariableValue {
    override val variableId: Int = ID

    companion object {
        const val ID = 545
    }
}

abstract class HomeSize : VariableValue {
    override val variableId: Int = ID

    companion object {
        const val ID = 933
    }
}

enum class DataType {
    INTEGER, DOUBLE, PERCENTAGE, MONETARY
}

// Values

object PercentageOfSingleOwnerHomes : DemographicConceptsVariable() {
    override val value = 366833
}

object PercentageOfPopulation65OrMore : DemographicConceptsVariable() {
    override val value = 366831
}

object PercentageOfPopulation18OrYounger : DemographicConceptsVariable() {
    override val value = 366830
}

object AveragePopulationAge : DemographicConceptsVariable() {
    override val value = 274520
}

object BaseData : DataTypeVariable() {
    override val value: Int = 72
}

object AnnualVariation : DataTypeVariable() {
    override val value: Int = 74
}

object GrossIncomePerHome : AccountingBalances() {
    override val value: Int = 382444
}

object GrossIncomePerPerson : AccountingBalances() {
    override val value: Int = 382443
}

object TotalOfHomeTypes : HomeType() {
    override val value: Int = 283862
}

object TotalOfHomeSize : HomeSize() {
    override val value: Int = 391802
}