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
import me.benguiman.spainstats.domain.GetAdrhForMunicipality
import me.benguiman.spainstats.domain.GetProvincesAndMunicipalitiesUseCase
import javax.inject.Inject

@HiltViewModel
class MunicipalityStatsViewModel @Inject constructor(
    private val getProvincesAndMunicipalitiesUseCase: GetProvincesAndMunicipalitiesUseCase,
    private val getAdrhForMunicipality: GetAdrhForMunicipality
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

    fun getProvincesAndMunicipalities() {
        getProvincesAndMunicipalitiesJob?.cancel()

        getProvincesAndMunicipalitiesJob = viewModelScope.launch {
            try {
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
                    it.copy(provinceMunicipalityList = items)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.toString())
                _municipalityHomeUiState.update {
                    it.copy(errorMessage = "Error retrieving Municipality")
                }
            }
        }
    }

    private var getMunicipalityStatsJob: Job? = null
    private val _municipalityStatUiState = MutableStateFlow(MunicipalityStatUiState())

    val municipalityStatUiState
        get() = _municipalityStatUiState.asStateFlow()

    fun getMunicipalityStats(id: Int) {
//        getMunicipalityStatsJob?.cancel()
        _municipalityStatUiState.update {
            it.copy(
                loading = false,

                )
        }
        getMunicipalityStatsJob = viewModelScope.launch {
            try {
                _municipalityStatUiState.update {
                    it.copy(loading = true)
                }
                val municipalityStatList = getAdrhForMunicipality(id)
                _municipalityStatUiState.update {
                    it.copy(
                        loading = false,
                        municipalityStatList = municipalityStatList
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.toString())
                _municipalityStatUiState.update {
                    it.copy(
                        loading = false,
                        errorMessage = "Error retrieving Municipality Stats"
                    )
                }
            }
        }
    }
}