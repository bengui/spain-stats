package me.benguiman.spainstats

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface SpainStatsDestination {
    val route: String
    val title: String
}

object Home : SpainStatsDestination {
    override val route = "home"
    override val title = "Pick a place"
}

object Municipality : SpainStatsDestination {
    const val baseRoute = "municipality_stats"
    const val municipalityIdArg = "municipality_id"
    const val municipalityCodeArg = "municipality_code"
    override val route = "${baseRoute}/{$municipalityIdArg}/{$municipalityCodeArg}"
    override val title = "Municipality Stats"
    val arguments = listOf(
        navArgument(municipalityIdArg) { type = NavType.IntType },
        navArgument(municipalityCodeArg) { type = NavType.StringType }
    )
}

val spainStatsScreens = listOf(Home, Municipality)