package me.benguiman.spainstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import me.benguiman.spainstats.ui.MunicipalityStatsViewModel
import me.benguiman.spainstats.ui.ProvinceMunicipalityListItemUiState
import me.benguiman.spainstats.ui.theme.SpainStatsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StatsApp()
        }
    }

}

@Composable
fun StatsApp(
    viewModel: MunicipalityStatsViewModel = viewModel()
) {
    val municipalityStatsUiState by viewModel.municipalityStatsUiState.collectAsState()
    SpainStatsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            if (municipalityStatsUiState.errorMessage.isEmpty()) {
                ProvinceMunicipalityList(provinceMunicipalityList = municipalityStatsUiState.provinceMunicipalityList)
            } else {
                Text(text = municipalityStatsUiState.errorMessage)
            }
        }
    }
}

@Composable
fun ProvinceMunicipalityList(
    provinceMunicipalityList: List<ProvinceMunicipalityListItemUiState>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(provinceMunicipalityList) {
            if (it.title) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = it.name,
                style = if (it.title) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpainStatsTheme {
        Greeting("Android")
    }
}