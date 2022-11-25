package me.benguiman.spainstats.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import me.benguiman.spainstats.R
import me.benguiman.spainstats.ui.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    showSnackBar: (StatsSnackbarData) -> Unit,
    onMunicipalityClickListener: (MunicipalityUiState) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val municipalityStatsUiState by viewModel.municipalityHomeUiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getProvincesAndMunicipalities()
    }
    val onErrorAction = { viewModel.getProvincesAndMunicipalities() }
    HomeScreenBody(
        modifier,
        municipalityStatsUiState,
        showSnackBar,
        onErrorAction,
        onMunicipalityClickListener
    )
}

@Composable
private fun HomeScreenBody(
    modifier: Modifier = Modifier,
    municipalityStatsUiState: MunicipalityHomeUiState,
    showSnackBar: (StatsSnackbarData) -> Unit = {},
    onErrorAction: () -> Unit = {},
    onMunicipalityClickListener: (MunicipalityUiState) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        when (municipalityStatsUiState.screenStatus) {
            ScreenLoading -> SpainStatsCircularProgressIndicator(
                modifier,
                stringResource(id = R.string.loading_places_data)
            )
            ScreenError -> LaunchErrorSnackBar(showSnackBar, onErrorAction)
            ScreenSuccess -> MunicipalitySearchScreen(
                onMunicipalitySelected = onMunicipalityClickListener,
                municipalityHomeUiState = municipalityStatsUiState,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun LaunchErrorSnackBar(
    showSnackBar: (StatsSnackbarData) -> Unit,
    onErrorAction: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        showSnackBar(
            StatsSnackbarData(
                message = context.getString(R.string.error_retrieving_locations_list),
                actionLabel = context.getString(R.string.retry_button),
                withDismissAction = false,
                isError = true,
                onAction = {
                    scope.launch {
                        onErrorAction()
                    }
                }
            )
        )
    }
}

@Composable
fun MunicipalitySearchScreen(
    modifier: Modifier = Modifier,
    onMunicipalitySelected: (MunicipalityUiState) -> Unit = {},
    municipalityHomeUiState: MunicipalityHomeUiState
) {
    Row {
        MunicipalityAutocompleteSearchField(
            municipalityHomeUiState,
            onMunicipalitySelected,
            modifier
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 96.dp)
            .fillMaxHeight()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.home_work),
            tint = MaterialTheme.colorScheme.surfaceVariant,
            contentDescription = null,
            modifier = Modifier.size(192.dp)
        )
    }
}

/**
 * https://github.com/androidx/androidx/blob/bea0ae031bd927b686fafd70c6f448b7c48da23d/compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/ExposedDropdownMenuSamples.kt
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MunicipalityAutocompleteSearchField(
    municipalityHomeUiState: MunicipalityHomeUiState,
    onMunicipalitySelected: (MunicipalityUiState) -> Unit = {},
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(true) }
    val municipalityAutocompleteState =
        remember { MunicipalityAutocompleteState(municipalityHomeUiState.municipalityList) }
    var selectedOptionText by remember { mutableStateOf("") }
    val municipalityList by produceState(
        key1 = selectedOptionText,
        initialValue = emptyList()
    ) {
        value =
            municipalityAutocompleteState.filterMunicipalityList(selectedOptionText.trim()).also {
                expanded = true
            }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            value = selectedOptionText,
            onValueChange = {
                selectedOptionText = it
            },
            label = { Text(stringResource(R.string.municipality_title)) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    municipalityList.firstOrNull()?.let { onMunicipalitySelected(it) }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        if (municipalityList.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize(false)
            ) {
                municipalityList.forEach { municipality ->
                    DropdownMenuItem(
                        text = { Text(municipality.name + " (" + municipality.provinceName + ")") },
                        onClick = {
                            onMunicipalitySelected(municipality)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun MunicipalitySearchScreenPreview() {
    HomeScreenBody(municipalityStatsUiState = MunicipalityHomeUiState(screenStatus = ScreenSuccess))
}