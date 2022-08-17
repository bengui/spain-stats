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

    private val _municipalityStatsUiState: MutableStateFlow<MunicipalityHomeUiState> =
        MutableStateFlow(MunicipalityHomeUiState())

    val municipalityStatsUiState
        get() = _municipalityStatsUiState.asStateFlow()

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

                _municipalityStatsUiState.update {
                    it.copy(provinceMunicipalityList = items)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.toString())
                _municipalityStatsUiState.update {
                    it.copy(errorMessage = "Error retrieving Municipality")
                }
            }
        }
    }

    private var getMunicipalityStatsJob: Job? = null

    fun getMunicipalityStats(id: Int) {
        getMunicipalityStatsJob?.cancel()
        getMunicipalityStatsJob = viewModelScope.launch {
            try {
                getAdrhForMunicipality(id)
            } catch (e: Exception) {

            }
        }
    }
}