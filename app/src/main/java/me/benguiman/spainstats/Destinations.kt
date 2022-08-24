package me.benguiman.spainstats

import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface SpainStatsDestination {
    val route: String
    val title: Int
}

object Home : SpainStatsDestination {
    override val route = "home"

    @StringRes
    override val title = R.string.home_title
}

object Municipality : SpainStatsDestination {
    const val baseRoute = "municipality_stats"
    const val municipalityIdArg = "municipality_id"
    const val municipalityCodeArg = "municipality_code"
    override val route = "${baseRoute}/{$municipalityIdArg}/{$municipalityCodeArg}"

    @StringRes
    override val title = R.string.municipality_title
    val arguments = listOf(
        navArgument(municipalityIdArg) { type = NavType.IntType },
        navArgument(municipalityCodeArg) { type = NavType.StringType }
    )
}

val spainStatsScreens = listOf(Home, Municipality)