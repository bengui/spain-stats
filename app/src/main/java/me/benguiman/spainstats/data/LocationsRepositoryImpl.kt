package me.benguiman.spainstats.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import me.benguiman.spainstats.data.network.IneService
import me.benguiman.spainstats.data.network.IneService.Companion.MUNICIPALITY_RESPONSE_TOTAL_PAGES
import me.benguiman.spainstats.data.network.VariableValueDto
import me.benguiman.spainstats.di.IoDispatcher
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val ineService: IneService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LocationsRepository {

    private var provinceWithMunicipalitiesList: List<Province>? = null

    private val mutex = Mutex()

    override suspend fun getAutonomousCommunityList(): List<AutonomousCommunity> {
        return withContext(coroutineDispatcher) {
            transformVariableValueIntoAutonomousCommunity(
                ineService.getVariableValues(variableId = AUTONOMOUS_COMMUNITY_VARIABLE_KEY)
            )
        }
    }

    override suspend fun getProvinceListPopulatedWithMunicipalities(): List<Province> {
        provinceWithMunicipalitiesList?.let {
            return it
        }

        return withContext(coroutineDispatcher) {
            mutex.withLock {

                provinceWithMunicipalitiesList?.let {
                    return@withContext it
                }

                provinceWithMunicipalitiesList = transformVariableValueIntoProvinceWithMunicipality(
                    provinceVariableList = getProvinceVariableList(),
                    municipalityVariableList = getMunicipalityVariableList()
                )

                provinceWithMunicipalitiesList!!
            }
        }
    }

    override suspend fun getMunicipality(municipalityId: Int): Municipality =
        transformVariableValueIntoMunicipality(getMunicipalityVariableList().first { it.id == municipalityId })


    private suspend fun getProvinceVariableList() =
        filterNonValidProvinces(ineService.getVariableValues(variableId = PROVINCE_VARIABLE_KEY))

    private suspend fun getMunicipalityVariableList(): List<VariableValueDto> {
        val municipalityList = mutableListOf<VariableValueDto>()
        for (i in 1..MUNICIPALITY_RESPONSE_TOTAL_PAGES) {
            municipalityList.addAll(
                ineService.getVariableValues(
                    variableId = MUNICIPALITY_VARIABLE_KEY,
                    page = i
                )
            )
        }

        return municipalityList
    }

    private fun filterNonValidProvinces(provinceList: List<VariableValueDto>): List<VariableValueDto> {
        return provinceList.filter {
            val trimmedCode = it.code.replace("0", "")
            trimmedCode.isNotEmpty()
        }
    }
}