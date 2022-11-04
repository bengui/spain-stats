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
import me.benguiman.spainstats.domain.GetDataForMunicipalityUseCase
import me.benguiman.spainstats.ui.ScreenError
import me.benguiman.spainstats.ui.ScreenLoading
import me.benguiman.spainstats.ui.ScreenSuccess
import javax.inject.Inject

@HiltViewModel
class MunicipalityStatsViewModel @Inject constructor(
    private val getDataForMunicipalityUseCase: GetDataForMunicipalityUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val TAG = "MunicipalityStatsViewModel"
    }

    private val _municipalityStatUiState: MutableStateFlow<MunicipalityStatUiState> =
        MutableStateFlow(
            MunicipalityStatUiState(
                municipalityStatList = emptyList(),
                screenStatus = ScreenLoading
            )
        )

    val municipalityStatUiState
        get() = _municipalityStatUiState.asStateFlow()

    fun getMunicipalityStats(
    ) {
        viewModelScope.launch {
            _municipalityStatUiState.update {
                MunicipalityStatUiState(
                    municipalityStatList = emptyList(),
                    screenStatus = ScreenLoading
                )
            }
            try {
                val municipalityId: Int = savedStateHandle[municipalityIdArg]
                    ?: throw IllegalStateException("municipality id is mandatory")
                val municipalityCode: String = savedStateHandle[municipalityCodeArg]
                    ?: throw IllegalStateException("municipality code is mandatory")
                Log.d(TAG, "getMunicipalityStats $municipalityId $municipalityCode}")
                val municipalityStatReport =
                    getDataForMunicipalityUseCase(municipalityId, municipalityCode)
                if (municipalityStatReport.municipalityStatList.isNotEmpty()) {
                    _municipalityStatUiState.update {
                        MunicipalityStatUiState(
                            municipalityStatList = municipalityStatReport.municipalityStatList,
                            municipalityName = municipalityStatReport.municipalityName,
                            screenStatus = ScreenSuccess
                        )
                    }
                } else {
                    _municipalityStatUiState.update {
                        MunicipalityStatUiState(
                            error = NoDataError,
                            screenStatus = ScreenError
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.toString())
                _municipalityStatUiState.update {
                    MunicipalityStatUiState(
                        error = ResponseError,
                        screenStatus = ScreenError
                    )
                }
            }
        }
    }
}