package me.benguiman.spainstats.ui

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable


@Composable
fun StatsSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(snackbarHostState)
}

data class StatsSnackbarData(
    val message: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = true,
    val onDismiss: () -> Unit = {},
    val onAction: () -> Unit = {},
    val isError: Boolean = false
)

suspend fun SnackbarHostState.showSnackBar(data: StatsSnackbarData) {
    this.showSnackbar(
        message = data.message,
        actionLabel = data.actionLabel,
        withDismissAction = data.withDismissAction,
    ).let {
        when (it) {
            SnackbarResult.ActionPerformed -> data.onAction()
            SnackbarResult.Dismissed -> data.onDismiss()
        }
    }
}
