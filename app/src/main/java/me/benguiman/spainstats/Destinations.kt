package me.benguiman.spainstats

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface SpainStatsDestination {
    val route: String
    val title: String
}

object Home : SpainStatsDestination {
    override val route = "home"
    override val title = "Municipalities"
}

object Municipality : SpainStatsDestination {
    override val route = "municipality_stats"
    override val title = "Municipality Stats"
    const val municipalityIdArg = "municipality_id"
    const val municipalityCodeArg = "municipality_code"
    val routeWithArgs = "$route/{$municipalityIdArg}/{$municipalityCodeArg}"
    val arguments = listOf(
        navArgument(municipalityIdArg) { type = NavType.IntType },
        navArgument(municipalityCodeArg) { type = NavType.StringType }
    )
}

val spainStatsScreens = listOf(Home, Municipality)