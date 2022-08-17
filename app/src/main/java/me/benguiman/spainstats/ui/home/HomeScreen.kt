package me.benguiman.spainstats.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.benguiman.spainstats.ui.MunicipalityStatsViewModel
import me.benguiman.spainstats.ui.ProvinceMunicipalityListItem

@Composable
fun HomeScreen(
    viewModel: MunicipalityStatsViewModel = viewModel(),
    onMunicipalityClickListener: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val municipalityStatsUiState by viewModel.municipalityHomeUiState.collectAsState()

    if (municipalityStatsUiState.errorMessage.isEmpty()) {
        ProvinceMunicipalityList(
            provinceMunicipalityList = municipalityStatsUiState.provinceMunicipalityList,
            onMunicipalityClickListener = onMunicipalityClickListener
        )
    } else {
        Text(text = municipalityStatsUiState.errorMessage)
    }

}

@Composable
fun ProvinceMunicipalityList(
    provinceMunicipalityList: List<ProvinceMunicipalityListItem>,
    onMunicipalityClickListener: (Int) -> Unit = {},
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
                    onMunicipalityClickListener(it.id)
                }) {
                    Text(it.name)
                }
            }
        }
    }
}