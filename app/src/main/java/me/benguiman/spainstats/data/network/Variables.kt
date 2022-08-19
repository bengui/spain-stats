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
    override val value: Int = ID

    companion object {
        const val ID = 482
    }
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

object MedianPopulationAge : DemographicConceptsVariable() {
    override val value = 274520
}

object BaseData : DataTypeVariable() {
    override val value: Int = 72
}

object GrossIncomePerHome : AccountingBalances() {
    override val variableId: Int = 382444
}

object GrossIncomePerPerson : AccountingBalances() {
    override val variableId: Int = 382443
}