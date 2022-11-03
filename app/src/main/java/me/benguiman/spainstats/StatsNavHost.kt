package me.benguiman.spainstats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.benguiman.spainstats.ui.StatsSnackbarData
import me.benguiman.spainstats.ui.home.HomeScreen
import me.benguiman.spainstats.ui.municipality.MunicipalityScreen

@Composable
fun StatsNavHost(
    navHostController: NavHostController,
    showSnackBar: (StatsSnackbarData) -> Unit,
    dismissSnackBar: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                showSnackBar = showSnackBar,
                onMunicipalityClickListener = { municipality ->
                    navHostController.navigateToMunicipality(municipality.id, municipality.code)
                })
        }
        composable(
            route = Municipality.route,
            arguments = Municipality.arguments
        ) {
            MunicipalityScreen(
                showSnackBar = showSnackBar,
                dismissSnackBar = dismissSnackBar
            )
        }
    }
}

private fun NavHostController.navigateToMunicipality(id: Int, code: String) =
    this.navigateSingleTop("${Municipality.baseRoute}/$id/$code")


fun NavHostController.navigateSingleTop(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTop.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
