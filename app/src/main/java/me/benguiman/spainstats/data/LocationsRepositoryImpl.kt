package me.benguiman.spainstats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.network.IneService
import me.benguiman.spainstats.data.network.VariableValueDto
import me.benguiman.spainstats.di.IoDispatcher
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val ineService: IneService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LocationsRepository {
    override suspend fun getAutonomousCommunityList(): List<AutonomousCommunity> {
        return withContext(coroutineDispatcher) {
            transformVariableValueIntoAutonomousCommunity(
                ineService.getVariableValues(variableId = AUTONOMOUS_COMMUNITY_VARIABLE_KEY)
            )
        }
    }

    override suspend fun getProvinceList(): List<Province> {
        return withContext(coroutineDispatcher) {
            transformVariableValueIntoProvince(
                ineService.getVariableValues(variableId = PROVINCE_VARIABLE_KEY)
            )
        }
    }

    override suspend fun getMunicipalityList(province: Province): List<Municipality> {
        return withContext(coroutineDispatcher) {
            transformVariableValueIntoMunicipality(
                filterMunicipalitiesFromProvince(
                    provinceCode = province.code,
                    municipalityList = getMunicipalityVariableList()
                )
            )
        }
    }

    override suspend fun getProvinceListPopulatedWithMunicipalities(): List<Province> {
        return withContext(coroutineDispatcher) {
            transformVariableValueIntoProvinceWithMunicipality(
                provinceVariableList = getProvinceVariableList(),
                municipalityVariableList = getMunicipalityVariableList()
            )
        }
    }

    private suspend fun getProvinceVariableList() =
        filterNonValidProvinces(ineService.getVariableValues(variableId = PROVINCE_VARIABLE_KEY))

    private suspend fun getMunicipalityVariableList() =
        ineService.getVariableValues(variableId = MUNICIPALITY_VARIABLE_KEY)

    private fun filterNonValidProvinces(provinceList: List<VariableValueDto>): List<VariableValueDto> {
        return provinceList.filter {
            val trimmedCode = it.code.replace("0", "")
            trimmedCode.isNotEmpty()
        }
    }
}