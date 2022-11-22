package me.benguiman.spainstats.ui.municipality

import android.icu.text.NumberFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
        Row(verticalAlignment = CenterVertically, modifier = Modifier.padding(8.dp)) {
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
            Row(verticalAlignment = CenterVertically) {
                Icon(
                    //TODO Define an icon per section
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
            when (multiElementRowUi.id) {
                ReportRowUi.POPULATION_ROW -> PopulationRow(multiElementRowUi, modifier)
                else -> DefaultMultiElementRow(multiElementRowUi, modifier)
            }

        }
    }
}

@Composable
fun DefaultMultiElementRow(
    multiElementRowUi: MultiElementRowUi,
    modifier: Modifier = Modifier
) {
    multiElementRowUi.statsList.forEach { statUi ->
        Row(verticalAlignment = CenterVertically) {
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
    }

}

@Composable
fun PopulationRow(
    multiElementRowUi: MultiElementRowUi,
    modifier: Modifier = Modifier
) {
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val secondaryContainerColor = MaterialTheme.colorScheme.onSecondary
    val colors = remember {
        mutableListOf(
            primaryContainerColor,
            secondaryContainerColor
        )
    }
    val colorMunicipalityMap = remember {
        multiElementRowUi.statsList.filter {
            it.dataType == DataType.PERCENTAGE
        }
            .take(2)
            .associateBy { colors.removeFirst() }
    }

    val colorPercentageMap = remember {
        colorMunicipalityMap.map { it.key to (it.value.value / 100 * 360).toFloat() }.toMap()
    }

    Row {
        Column(
            modifier = Modifier
                .weight(3f)
                .height(100.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            colorMunicipalityMap.forEach { entry ->
                Row(verticalAlignment = CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.circle),
                        tint = entry.key,
                        contentDescription = null,
                        modifier = Modifier
                            .size(15.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = stringResource(entry.value.name, entry.value.value.toInt()),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
        Column(Modifier.weight(1f)) {
            PieChart(
                colorPercentageMap,
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        }
    }

    val intMunicipalityValues = remember {
        multiElementRowUi.statsList.filter {
            it.dataType != DataType.PERCENTAGE
        }
    }

    intMunicipalityValues.forEach { municipalityUi ->
        Row(verticalAlignment = CenterVertically) {
            Text(
                text = stringResource(municipalityUi.name),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = formatValue(municipalityUi),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PieChart(
    colorPercentageMap: Map<Color, Float>,
    backgroundColor: Color
) {
    Canvas(
        modifier = Modifier
            .size(size = 100.dp)
            .padding(vertical = 4.dp)
    ) {
        var previousAngle = 0f
        colorPercentageMap.forEach { entry ->
            drawArc(
                color = entry.key,
                startAngle = previousAngle,
                sweepAngle = entry.value,
                useCenter = true
            )
            previousAngle += entry.value
        }
        drawArc(
            color = backgroundColor,
            startAngle = previousAngle,
            sweepAngle = 360f - previousAngle,
            useCenter = true
        )
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
fun MunicipalityStatsPreview() {
    MunicipalityStats(
        "Berga", listOf(
            MultiElementRowUi(
                title = R.string.population_title,
                statsList = listOf(
                    MunicipalityStatUi(
                        name = R.string.percentage_of_population_65_or_more,
                        value = 23.0,
                        dataType = DataType.PERCENTAGE
                    ),
                    MunicipalityStatUi(
                        name = R.string.percentage_of_population_younger_than_18,
                        value = 16.0,
                        dataType = DataType.PERCENTAGE
                    ),
                    MunicipalityStatUi(
                        name = R.string.average_population_age,
                        value = 45.0,
                        dataType = DataType.INTEGER
                    )
                ),
                id = ReportRowUi.POPULATION_ROW
            ),
            MultiElementRowUi(
                title = R.string.average_gross_income_title,
                statsList = listOf(
                    MunicipalityStatUi(
                        name = R.string.gross_income_per_home,
                        value = 23547.00,
                        dataType = DataType.MONETARY
                    ),
                    MunicipalityStatUi(
                        name = R.string.gross_income_per_home,
                        value = 42631.00,
                        dataType = DataType.MONETARY
                    )
                )
            ),

            SimpleRowUi(
                MunicipalityStatUi(
                    R.string.buildings_count,
                    605.0,
                    DataType.INTEGER
                )
            ),
            SimpleRowUi(
                MunicipalityStatUi(
                    R.string.estate_count,
                    1405.0,
                    DataType.DOUBLE
                )
            ),
            SimpleRowUi(
                MunicipalityStatUi(
                    R.string.ipva_estate_annual_variation,
                    1.9,
                    DataType.DOUBLE
                )
            )
        )
    )
}