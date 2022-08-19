package me.benguiman.spainstats.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.benguiman.spainstats.domain.GetDataForMunicipality
import me.benguiman.spainstats.domain.GetProvincesAndMunicipalitiesUseCase
import javax.inject.Inject

@HiltViewModel
class MunicipalityStatsViewModel @Inject constructor(
    private val getProvincesAndMunicipalitiesUseCase: GetProvincesAndMunicipalitiesUseCase,
    private val getDataForMunicipality: GetDataForMunicipality
) : ViewModel() {

    companion object {
        const val TAG = "MunicipalityStatsViewModel"
    }

    private val _municipalityHomeUiState: MutableStateFlow<MunicipalityHomeUiState> =
        MutableStateFlow(MunicipalityHomeUiState())

    val municipalityHomeUiState
        get() = _municipalityHomeUiState.asStateFlow()

    private var getProvincesAndMunicipalitiesJob: Job? = null

    init {
        getProvincesAndMunicipalities()
    }

    private fun getProvincesAndMunicipalities() {
        getProvincesAndMunicipalitiesJob?.cancel()

        getProvincesAndMunicipalitiesJob = viewModelScope.launch {
            try {
                _municipalityHomeUiState.update {
                    MunicipalityHomeUiState(loading = true)
                }

                val provinceList = getProvincesAndMunicipalitiesUseCase()
                val items = provinceList.fold(
                    mutableListOf<ProvinceMunicipalityListItem>()
                ) { acc, province ->
                    acc.add(ProvinceMunicipalityListItem(name = province.name, title = true))
                    acc.addAll(province.municipalityList.map {
                        ProvinceMunicipalityListItem(
                            id = it.id,
                            name = it.name
                        )
                    })
                    acc
                }

                _municipalityHomeUiState.update {
                    MunicipalityHomeUiState(provinceMunicipalityList = items)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.toString())
                _municipalityHomeUiState.update {
                    MunicipalityHomeUiState(errorMessage = "Error retrieving Municipality")
                }
            }
        }
    }


    suspend fun getMunicipalityStats(id: Int): MunicipalityStatUiState {
        Log.d(TAG, "getMunicipalityStats $id")

        return try {
            val municipalityStatList = getDataForMunicipality(id)
            if (municipalityStatList.isNotEmpty()) {
                MunicipalityStatUiState(
                    loading = false,
                    municipalityStatList = municipalityStatList
                )
            } else {
                MunicipalityStatUiState(
                    loading = false,
                    errorMessage = "Empty Data"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: e.toString())
            MunicipalityStatUiState(
                loading = false,
                errorMessage = "Error retrieving Municipality Stats"
            )
        }

    }
}