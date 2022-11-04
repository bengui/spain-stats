package me.benguiman.spainstats.ui.municipality

import android.icu.text.NumberFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.benguiman.spainstats.R
import me.benguiman.spainstats.domain.models.DataType
import me.benguiman.spainstats.ui.*
import java.util.*

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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        when (municipalityStatUiState.screenStatus) {
            ScreenLoading -> SpainStatsCircularProgressIndicator(modifier)
            ScreenError -> processError(municipalityStatUiState, showSnackBar, scope, viewModel)
            ScreenSuccess -> MunicipalityStats(
                municipalityName = municipalityStatUiState.municipalityName,
                municipalityStatList = municipalityStatUiState.municipalityStatList,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun processError(
    municipalityStatUiState: MunicipalityStatUiState,
    showSnackBar: (StatsSnackbarData) -> Unit,
    scope: CoroutineScope,
    viewModel: MunicipalityStatsViewModel
) {
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
}

@Composable
fun MunicipalityStats(
    municipalityName: String = "",
    municipalityStatList: List<MunicipalityStat> = emptyList(),
    modifier: Modifier = Modifier
) {
    Text(
        text = municipalityName,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.padding(vertical = 8.dp)
    )
    LazyColumn {
        items(municipalityStatList) {
            MunicipalityStatsCard(it)
        }
    }
}

@Composable
private fun MunicipalityStatsCard(
    municipalityStat: MunicipalityStat,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(bottom = 8.dp)) {
        Column(Modifier.padding(8.dp)) {
            Text(
                text = municipalityStat.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = formatValue(municipalityStat),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun formatValue(municipalityStat: MunicipalityStat): String {
    val spainLocale = Locale("es", "ES")
    val numberFormat = NumberFormat.getInstance(spainLocale)
    val currencyFormat = NumberFormat.getCurrencyInstance(spainLocale)
    currencyFormat.maximumFractionDigits = 0
    val percentageFormat = NumberFormat.getPercentInstance()
    return when (municipalityStat.dataType) {
        DataType.INTEGER -> numberFormat.format(municipalityStat.value.toInt())
        DataType.DOUBLE -> numberFormat.format(municipalityStat.value)
        DataType.MONETARY -> currencyFormat.format((municipalityStat.value.toInt()))
        DataType.PERCENTAGE -> percentageFormat.format(municipalityStat.value / 100)
    }
}


@Preview
@Composable
fun MunicipalityStatsCardPreview() {
    MunicipalityStatsCard(
        MunicipalityStat(
            name = "Berga. Variación anual. Total viviendas. Total.",
            value = 1.8,
            dataType = DataType.INTEGER
        )
    )
}

@Preview
@Composable
fun MunicipalityStatsPreview() {
    MunicipalityStats(
        "Berga", listOf(
            MunicipalityStat(
                "Berga. Variación anual. Total viviendas. Total.",
                1.9,
                DataType.INTEGER
            ),
            MunicipalityStat("Berga. Índice. Total viviendas. Total.", 107.605, DataType.INTEGER),
            MunicipalityStat("Berga. Ponderación.", 0.405, DataType.DOUBLE)
        )
    )
}