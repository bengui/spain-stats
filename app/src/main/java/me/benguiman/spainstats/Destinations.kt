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
    val routeWithArgs = "$route/{$municipalityIdArg}"
    val arguments = listOf(
        navArgument(municipalityIdArg) { type = NavType.IntType }
    )
}

val spainStatsScreens = listOf(Home, Municipality)