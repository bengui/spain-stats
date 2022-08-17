package me.benguiman.spainstats.ui.municipality

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.benguiman.spainstats.ui.MunicipalityStatsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import me.benguiman.spainstats.MunicipalityStat

@Composable
fun MunicipalityScreen(
    viewModel: MunicipalityStatsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val municipalityStatUiState by viewModel.municipalityStatUiState.collectAsState()

    if (municipalityStatUiState.loading) {
        Text("Loading...")
    } else if (municipalityStatUiState.errorMessage.isNotEmpty()) {
        Text(municipalityStatUiState.errorMessage)
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