package me.benguiman.spainstats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.network.IneService
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
                    province.code,
                    ineService.getVariableValues(variableId = MUNICIPALITY_VARIABLE_KEY)
                )
            )
        }
    }

    override suspend fun getProvinceListPopulatedWithMunicipalities(): List<Province> {
        return withContext(coroutineDispatcher) {

            val municipalityVariableList =
                ineService.getVariableValues(variableId = MUNICIPALITY_VARIABLE_KEY)
            val provinceVariableList = ineService.getVariableValues(variableId = PROVINCE_VARIABLE_KEY)
            transformVariableValueIntoProvinceWithMunicipality(
                provinceVariableList,
                municipalityVariableList
            )
        }
    }
}