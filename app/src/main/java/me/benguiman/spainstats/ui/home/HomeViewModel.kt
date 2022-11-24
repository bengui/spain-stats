package me.benguiman.spainstats.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.benguiman.spainstats.data.Municipality
import me.benguiman.spainstats.data.Province
import me.benguiman.spainstats.domain.GetProvincesAndMunicipalitiesUseCase
import me.benguiman.spainstats.ui.MunicipalityStatUi
import me.benguiman.spainstats.ui.ScreenError
import me.benguiman.spainstats.ui.ScreenLoading
import me.benguiman.spainstats.ui.ScreenSuccess
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProvincesAndMunicipalitiesUseCase: GetProvincesAndMunicipalitiesUseCase
) : ViewModel() {
    companion object {
        const val TAG = "HomeViewModel"
    }

    private val _municipalityHomeUiState: MutableStateFlow<MunicipalityHomeUiState> =
        MutableStateFlow(MunicipalityHomeUiState(screenStatus = ScreenLoading))

    val municipalityHomeUiState
        get() = _municipalityHomeUiState.asStateFlow()

    private var getProvincesAndMunicipalitiesJob: Job? = null

    fun getProvincesAndMunicipalities() {
        getProvincesAndMunicipalitiesJob?.cancel()

        getProvincesAndMunicipalitiesJob = viewModelScope.launch {
            try {
                _municipalityHomeUiState.update {
                    MunicipalityHomeUiState(screenStatus = ScreenLoading)
                }

                //TODO Display the province name along with the municipality name in the autocomplete. Municipality (Province)
                val provinceList = getProvincesAndMunicipalitiesUseCase()
                val items = provinceList.fold(
                    mutableListOf<ProvinceMunicipalityListItem>()
                ) { acc, province ->
                    acc.add(ProvinceMunicipalityListItem(name = province.name, title = true))
                    acc.addAll(province.municipalityList.map {
                        ProvinceMunicipalityListItem(
                            id = it.id,
                            name = it.name,
                            code = it.code
                        )
                    })
                    acc
                }

                val municipalityList =
                    provinceList.fold(mutableListOf<MunicipalityUiState>()) { list, province ->
                        list.addAll(provinceToMunicipalityHomeUiStateList(province))
                        list
                    }.sortedBy { it.name }

                _municipalityHomeUiState.update {
                    MunicipalityHomeUiState(
                        provinceMunicipalityList = items,
                        municipalityList = municipalityList,
                        screenStatus = ScreenSuccess
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.toString())
                _municipalityHomeUiState.update {
                    MunicipalityHomeUiState(screenStatus = ScreenError)
                }
            }
        }
    }

    private fun provinceToMunicipalityHomeUiStateList(province: Province): List<MunicipalityUiState> {
        return province.municipalityList.fold(mutableListOf()) { list, municipality ->
            list.add(
                MunicipalityUiState(
                    id = municipality.id,
                    name = municipality.name,
                    code = municipality.code,
                    provinceName = province.name
                )
            )
            list
        }
    }
}