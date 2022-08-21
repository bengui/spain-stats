package me.benguiman.spainstats.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.benguiman.spainstats.ui.MunicipalityStatsViewModel
import me.benguiman.spainstats.ui.MunicipalityUiState
import me.benguiman.spainstats.ui.ProvinceMunicipalityListItem

@Composable
fun HomeScreen(
    viewModel: MunicipalityStatsViewModel = viewModel(),
    onMunicipalityClickListener: (MunicipalityUiState) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val municipalityStatsUiState by viewModel.municipalityHomeUiState.collectAsState()

    if (municipalityStatsUiState.errorMessage.isNotEmpty()) {
        Text(text = municipalityStatsUiState.errorMessage)
    } else if (municipalityStatsUiState.loading) {
        Text(text = "Loading...")
    } else {
        MunicipalityAutocompleteField(
            municipalityList = municipalityStatsUiState.municipalityList,
            onMunicipalitySelected = onMunicipalityClickListener
        )
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
    municipalityList: List<MunicipalityUiState>,
    onMunicipalitySelected: (MunicipalityUiState) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedOptionText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val municipalityAutocompleteState = remember { MunicipalityAutocompleteState(municipalityList) }
    val options by produceState(
        key1 = selectedOptionText,
        initialValue = emptyList()
    ) {
        value = municipalityAutocompleteState.filterMunicipalityList(selectedOptionText).also {
            //TODO Avoid updating this variable in here. It should work out of the box
            expanded = it.isNotEmpty()
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
            onValueChange = { newValue ->
                selectedOptionText = newValue
            },
            label = { Text("Municipality") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        if (options.isNotEmpty()) {
            ExposedDropdownMenu(expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { municipality ->
                    DropdownMenuItem(text = { Text(municipality.name) },
                        onClick = {
                            selectedOptionText = municipality.name
                            expanded = false
                            onMunicipalitySelected(municipality)
                        })
                }
            }
        }

    }
}