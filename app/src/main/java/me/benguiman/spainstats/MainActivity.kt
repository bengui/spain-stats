package me.benguiman.spainstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.benguiman.spainstats.ui.MunicipalityStatsViewModel
import me.benguiman.spainstats.ui.home.HomeScreen
import me.benguiman.spainstats.ui.municipality.MunicipalityScreen
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
    navHostController: NavHostController = rememberNavController()
) {
    SpainStatsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            StatsNavHos(navHostController = navHostController)
        }
    }
}

@Composable
fun StatsNavHos(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            val viewModel = hiltViewModel<MunicipalityStatsViewModel>()
            HomeScreen(
                viewModel = viewModel,
                onMunicipalityClickListener = { id, code ->
                    navHostController.navigateToMunicipality(id, code)
                })
        }
        composable(
            route = Municipality.routeWithArgs,
            arguments = Municipality.arguments
        ) { navBackStackEntry ->
            val municipalityId =
                navBackStackEntry
                    .arguments
                    ?.getInt(Municipality.municipalityIdArg) ?: -1

            val municipalityCode =
                navBackStackEntry
                    .arguments
                    ?.getString(Municipality.municipalityCodeArg) ?: ""

            val viewModel = hiltViewModel<MunicipalityStatsViewModel>()
            MunicipalityScreen(
                viewModel = viewModel,
                municipalityId = municipalityId,
                municipalityCode = municipalityCode
            )
        }
    }
}

private fun NavHostController.navigateToMunicipality(id: Int, code: String) =
    this.navigateSingleTop("${Municipality.route}/$id/$code")


fun NavHostController.navigateSingleTop(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTop.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
