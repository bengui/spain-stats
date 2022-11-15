package me.benguiman.spainstats.ui.municipality

import android.icu.text.NumberFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import kotlin.math.roundToInt

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
                municipalityStatList = municipalityStatUiState.municipalityStatReportRowUiList,
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
    municipalityStatList: List<ReportRowUi> = emptyList(),
    modifier: Modifier = Modifier
) {
    Text(
        text = municipalityName,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.padding(vertical = 8.dp)
    )
    LazyColumn {
        items(municipalityStatList) { reportRowUi ->
            when (reportRowUi) {
                is MultiElementRowUi -> MunicipalityStatsMultiElementCard(reportRowUi)
                is SimpleRowUi -> MunicipalityStatsCard(reportRowUi)
            }
        }
    }
}

@Composable
private fun MunicipalityStatsCard(
    simpleRowUi: SimpleRowUi,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(bottom = 8.dp)) {
        Column(Modifier.padding(8.dp)) {
            Text(
                text = stringResource(simpleRowUi.statUi.name),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = formatValue(simpleRowUi.statUi),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MunicipalityStatsMultiElementCard(
    multiElementRowUi: MultiElementRowUi,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(bottom = 8.dp)) {
        Column(Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(multiElementRowUi.title),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            multiElementRowUi.statsList.forEach { statUi ->
                Row {
                    Text(
                        text = stringResource(statUi.name),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = formatValue(statUi),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (statUi.dataType == DataType.PERCENTAGE) {
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val secondaryColor = MaterialTheme.colorScheme.primaryContainer
                    val percentage = remember {
                        (statUi.value * 360 / 100).toFloat()
                    }
                    Row {
                        Canvas(
                            modifier = Modifier
                                .size(size = 100.dp)
                        ) {
                            drawArc(
                                color = primaryColor,
                                startAngle = 0f,
                                sweepAngle = percentage,
                                useCenter = true
                            )
                            drawArc(
                                color = secondaryColor,
                                startAngle = percentage,
                                sweepAngle = 360f - percentage,
                                useCenter = true
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatValue(municipalityStat: MunicipalityStatUi): String {
    val spainLocale = Locale("es", "ES")
    val numberFormat = NumberFormat.getInstance(spainLocale)
    val currencyFormat = NumberFormat.getCurrencyInstance(spainLocale)
    currencyFormat.maximumFractionDigits = 0
    val percentageFormat = NumberFormat.getPercentInstance()
    return when (municipalityStat.dataType) {
        DataType.INTEGER -> numberFormat.format(municipalityStat.value.roundToInt())
        DataType.DOUBLE -> numberFormat.format(municipalityStat.value)
        DataType.MONETARY -> currencyFormat.format((municipalityStat.value.toInt()))
        DataType.PERCENTAGE -> percentageFormat.format(municipalityStat.value / 100)
    }
}


@Preview
@Composable
fun MunicipalityStatsCardPreview() {
    MunicipalityStatsCard(
        SimpleRowUi(
            MunicipalityStatUi(
                name = R.string.average_population_age,
                value = 45.0,
                dataType = DataType.INTEGER
            )
        )
    )
}

@Preview
@Composable
fun MunicipalityStatsPreview() {
    MunicipalityStats(
        "Berga", listOf(
            SimpleRowUi(
                MunicipalityStatUi(
                    R.string.ipva_estate_annual_variation,
                    1.9,
                    DataType.INTEGER
                )
            ),
            SimpleRowUi(
                MunicipalityStatUi(
                    R.string.buildings_count,
                    107.605,
                    DataType.INTEGER
                )
            ),
            SimpleRowUi(
                MunicipalityStatUi(R.string.estate_count, 0.405, DataType.DOUBLE)
            )
        )
    )
}