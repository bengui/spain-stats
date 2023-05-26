package me.benguiman.spainstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.benguiman.spainstats.ui.StatsSnackbarHost
import me.benguiman.spainstats.ui.showSnackBar
import me.benguiman.spainstats.ui.theme.SpainStatsTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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
                    title = stringResource(currentScreen.title),
                    screen = currentScreen,
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
    screen: SpainStatsDestination = Home,
    onBackPressed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 12.dp)
            )
        },
        navigationIcon =
        {
            when (screen) {
                Home -> {
                    Image(
                        painter = painterResource(id = R.drawable.es_flag),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
                else -> {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back_content_description)
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}


@Preview
@Composable
fun SpainStatsTopBarPreview() {
    SpainStatsTopBar(title = stringResource(Home.title))
}