package me.benguiman.spainstats.data

import me.benguiman.spainstats.data.network.VariableValueDto

fun transformVariableValueIntoAutonomousCommunity(variableList: List<VariableValueDto>): List<AutonomousCommunity> =
    variableList.map {
        AutonomousCommunity(
            id = it.id,
            name = it.name,
            code = it.code
        )
    }

fun transformVariableValueListIntoMunicipality(variableList: List<VariableValueDto>): List<Municipality> =
    variableList.map {
        transformVariableValueIntoMunicipality(it)
    }

fun transformVariableValueIntoMunicipality(it: VariableValueDto) =
    Municipality(
        id = it.id,
        name = it.name,
        code = it.code
    )


fun transformVariableValueIntoProvinceWithMunicipality(
    provinceVariableList: List<VariableValueDto>,
    municipalityVariableList: List<VariableValueDto>
): List<Province> =
    provinceVariableList
        .sortedByName()
        .map {
            Province(
                id = it.id,
                name = it.name,
                code = it.code,
                municipalityList = transformVariableValueListIntoMunicipality(
                    filterMunicipalitiesFromProvince(
                        it.code,
                        municipalityVariableList
                    ).sortedByName()
                )
            )
        }

fun filterMunicipalitiesFromProvince(
    provinceCode: String,
    municipalityList: List<VariableValueDto>
) =
    municipalityList.filter { it.code.startsWith(provinceCode) }

private fun List<VariableValueDto>.sortedByName() =
    this.sortedBy { it.name }