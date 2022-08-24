package me.benguiman.spainstats.ui.municipality

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import me.benguiman.spainstats.MunicipalityStat
import me.benguiman.spainstats.ui.StatsSnackbarData

@Composable
fun MunicipalityScreen(
    viewModel: MunicipalityStatsViewModel = hiltViewModel(),
    showSnackBar: (StatsSnackbarData) -> Unit,
    dismissSnackBar: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val municipalityStatUiState by viewModel.municipalityStatUiState.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getMunicipalityStats()
    }
    DisposableEffect(Unit) { onDispose { dismissSnackBar() } }

    if (municipalityStatUiState.loading) {
        Text("Loading...")
    } else if (municipalityStatUiState.errorMessage.isNotEmpty()) {
        LaunchedEffect(Unit) {
            showSnackBar(
                StatsSnackbarData(
                    message = municipalityStatUiState.errorMessage,
                    isError = true,
                    withDismissAction = false,
                    actionLabel = "Retry",
                    onAction = {
                        scope.launch {
                            viewModel.getMunicipalityStats()
                        }
                    }
                )
            )
        }
    } else {
        MunicipalityStats(municipalityStatUiState.municipalityStatList, modifier)
    }
}

@Composable
fun MunicipalityStats(
    municipalityStatList: List<MunicipalityStat> = emptyList(),
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(municipalityStatList) {
            Column {
                Text(it.name)
                Text(it.value.toString())
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}