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

fun transformVariableValueIntoProvince(variableList: List<VariableValueDto>): List<Province> =
    variableList.map {
        Province(
            id = it.id,
            name = it.name,
            code = it.code,
            municipalityList = emptyList()
        )
    }

fun transformVariableValueIntoMunicipality(variableList: List<VariableValueDto>): List<Municipality> =
    variableList.map {
        Municipality(
            id = it.id,
            name = it.name,
            code = it.code
        )
    }

fun transformVariableValueIntoProvinceWithMunicipality(
    provinceVariableList: List<VariableValueDto>,
    municipalityVariableList: List<VariableValueDto>
): List<Province> =
    provinceVariableList.map {
        Province(
            id = it.id,
            name = it.name,
            code = it.code,
            municipalityList = transformVariableValueIntoMunicipality(
                filterMunicipalitiesFromProvince(
                    it.code,
                    municipalityVariableList
                )
            )
        )
    }

fun filterMunicipalitiesFromProvince(
    provinceCode: String,
    municipalityList: List<VariableValueDto>
) =
    municipalityList.filter { it.code.startsWith(provinceCode) }