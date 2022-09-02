package me.benguiman.spainstats.ui.municipality

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import me.benguiman.spainstats.MunicipalityStat
import me.benguiman.spainstats.R
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
        Text(stringResource(id = R.string.loading_data), modifier = modifier.padding(8.dp))
    } else if (municipalityStatUiState.error != null) {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            val error = municipalityStatUiState.error
            val errorMessage = when (error) {
                is NoDataError -> context.getString(R.string.municipality_no_data_error)
                else -> context.getString(R.string.municipality_response_error)
            }
            val actionLabel = if (error != NoDataError) {
                context.getString(R.string.retry_button)
            } else {
                null
            }
            showSnackBar(
                StatsSnackbarData(
                    message = errorMessage,
                    isError = true,
                    withDismissAction = false,
                    actionLabel = actionLabel,
                    onAction = {
                        scope.launch {
                            viewModel.getMunicipalityStats()
                        }
                    }
                )
            )
        }
    } else {
        MunicipalityStats(
            municipalityName = municipalityStatUiState.municipalityName,
            municipalityStatList = municipalityStatUiState.municipalityStatList,
            modifier = modifier
        )
    }
}

@Composable
fun MunicipalityStats(
    municipalityName: String = "",
    municipalityStatList: List<MunicipalityStat> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        Text(
            text = municipalityName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier.padding(vertical = 8.dp)
        )
        LazyColumn {
            items(municipalityStatList) {
                MunicipalityStatsCard(it.name, it.value.toString())
            }
        }
    }
}

@Composable
private fun MunicipalityStatsCard(
    name: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(bottom = 8.dp)) {
        Column(Modifier.padding(8.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun MunicipalityStatsCardPreview() {
    MunicipalityStatsCard(
        name = "Berga. Variación anual. Total viviendas. Total.",
        value = "1.8"
    )
}

@Preview
@Composable
fun MunicipalityStatsPreview() {
    MunicipalityStats(
        "Berga", listOf(
            MunicipalityStat("Berga. Variación anual. Total viviendas. Total.", 1.9),
            MunicipalityStat("Berga. Índice. Total viviendas. Total.", 107.605),
            MunicipalityStat("Berga. Ponderación.", 0.405)
        )
    )
}