package me.benguiman.spainstats.ui.municipality

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.benguiman.spainstats.Municipality.municipalityCodeArg
import me.benguiman.spainstats.Municipality.municipalityIdArg
import me.benguiman.spainstats.domain.GetDataForMunicipality
import javax.inject.Inject

@HiltViewModel
class MunicipalityStatsViewModel @Inject constructor(
    private val getDataForMunicipality: GetDataForMunicipality,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val TAG = "MunicipalityStatsViewModel"
    }

    private val _municipalityStatUiState: MutableStateFlow<MunicipalityStatUiState> =
        MutableStateFlow(
            MunicipalityStatUiState(
                loading = false,
                municipalityStatList = emptyList()
            )
        )

    val municipalityStatUiState
        get() = _municipalityStatUiState.asStateFlow()

    fun getMunicipalityStats(
    ) {
        viewModelScope.launch {
            _municipalityStatUiState.update {
                MunicipalityStatUiState(
                    loading = true,
                    municipalityStatList = emptyList()
                )
            }
            try {
                val municipalityId: Int = savedStateHandle[municipalityIdArg]
                    ?: throw IllegalStateException("municipality id is mandatory")
                val municipalityCode: String = savedStateHandle[municipalityCodeArg]
                    ?: throw IllegalStateException("municipality code is mandatory")
                Log.d(TAG, "getMunicipalityStats $municipalityId $municipalityCode}")
                val municipalityStatReport =
                    getDataForMunicipality(municipalityId, municipalityCode)
                if (municipalityStatReport.municipalityStatList.isNotEmpty()) {
                    _municipalityStatUiState.update {
                        MunicipalityStatUiState(
                            loading = false,
                            municipalityStatList = municipalityStatReport.municipalityStatList,
                            municipalityName = municipalityStatReport.municipalityName
                        )
                    }
                } else {
                    _municipalityStatUiState.update {
                        MunicipalityStatUiState(
                            loading = false,
                            error = NoDataError
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.toString())
                _municipalityStatUiState.update {
                    MunicipalityStatUiState(
                        loading = false,
                        error = ResponseError
                    )
                }
            }
        }
    }
}