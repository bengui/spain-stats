package me.benguiman.spainstats.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import me.benguiman.spainstats.R
import me.benguiman.spainstats.ui.StatsSnackbarData

@Composable
fun HomeScreen(
    showSnackBar: (StatsSnackbarData) -> Unit,
    onMunicipalityClickListener: (MunicipalityUiState) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val municipalityStatsUiState by viewModel.municipalityHomeUiState.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getProvincesAndMunicipalities()
    }

    when (municipalityStatsUiState.homeScreenStatus) {
        HomeScreenError -> {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                showSnackBar(
                    StatsSnackbarData(
                        message = context.getString(R.string.error_retrieving_locations_list),
                        actionLabel = context.getString(R.string.retry_button),
                        withDismissAction = false,
                        isError = true,
                        onAction = {
                            scope.launch {
                                viewModel.getProvincesAndMunicipalities()
                            }
                        }
                    )
                )
            }
        }

        HomeScreenLoading -> Text(
            stringResource(id = R.string.loading_data),
            modifier = modifier.padding(8.dp)
        )
        HomeScreenSuccess -> {

            MunicipalityAutocompleteField(
                onMunicipalitySelected = onMunicipalityClickListener,
                municipalityHomeUiState = municipalityStatsUiState,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ProvinceMunicipalityList(
    provinceMunicipalityList: List<ProvinceMunicipalityListItem>,
    onMunicipalityClickListener: (Int, String) -> Unit = { _: Int, _: String -> },
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(provinceMunicipalityList) {
            if (it.title) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (it.title) {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                TextButton(onClick = {
                    onMunicipalityClickListener(it.id, it.code)
                }) {
                    Text(it.name)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MunicipalityAutocompleteField(
    onMunicipalitySelected: (MunicipalityUiState) -> Unit = {},
    municipalityHomeUiState: MunicipalityHomeUiState,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(true) }
    val municipalityAutocompleteState =
        remember { MunicipalityAutocompleteState(municipalityHomeUiState.municipalityList) }
    var selectedOptionText by remember { mutableStateOf("") }
    val municipalityList by produceState<List<MunicipalityUiState>>(
        key1 = selectedOptionText,
        initialValue = emptyList()
    ) {
        value = municipalityAutocompleteState.filterMunicipalityList(selectedOptionText).also {
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
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
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
                        text = { Text(municipality.name) },
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