package me.benguiman.spainstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.benguiman.spainstats.ui.StatsSnackbarHost
import me.benguiman.spainstats.ui.showSnackBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsApp(
    navHostController: NavHostController = rememberNavController()
) {

    val currentBackStack by navHostController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentRoute = currentDestination?.route ?: Home.route
    val currentScreen = spainStatsScreens.find { it.route == currentRoute } ?: Home
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SpainStatsTheme {
        Scaffold(
            snackbarHost = { StatsSnackbarHost(snackbarHostState) },
            topBar = {
                SpainStatsTopBar(
                    title = currentScreen.title,
                    displayBackButton = currentRoute != Home.route,
                    onBackPressed = {
                        navHostController.navigateUp()
                    }
                )
            }
        ) { internalPadding ->
            StatsNavHost(
                navHostController = navHostController,
                showSnackBar = { scope.launch { snackbarHostState.showSnackBar(it) } },
                dismissSnackBar = { scope.launch { snackbarHostState.currentSnackbarData?.dismiss() } },
                modifier = Modifier.padding(internalPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpainStatsTopBar(
    title: String,
    displayBackButton: Boolean = false,
    onBackPressed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    SmallTopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon =
        {
            if (displayBackButton) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        modifier = modifier
    )
}


@Preview
@Composable
fun SpainStatsTopBarPreview() {
    SpainStatsTopBar(title = "Municipality", displayBackButton = true)
}