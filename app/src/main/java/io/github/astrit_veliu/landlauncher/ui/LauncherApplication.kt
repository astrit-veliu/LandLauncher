package io.github.astrit_veliu.landlauncher.ui

import android.content.res.Resources
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.astrit_veliu.landlauncher.common.snackbar.SnackbarManager
import io.github.astrit_veliu.landlauncher.domain.model.Application
import io.github.astrit_veliu.landlauncher.ui.graphs.Graph
import io.github.astrit_veliu.landlauncher.ui.graphs.homeNavGraph
import io.github.astrit_veliu.landlauncher.ui.theme.LauncherTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun LauncherApp(
    isDarkModeEnabled: Boolean,
    homeApps: List<Application>?,
    allApps: List<Application>?,
    onBoardingComplete: () -> Unit,
    onDarkModeSet: (Boolean) -> Unit
) {
    //LauncherTheme(isDarkModeEnabled) {
    LauncherTheme() {
        //TransparentSystemBars()
        val appState = rememberAppState(homeApps, allApps)

        Scaffold(
            contentWindowInsets = WindowInsets.systemBars,
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(
                    hostState = appState.snackBarHostState,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(
                            bottom = WindowInsets.systemBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                                .plus(10.dp)
                        ),
                    snackbar = { snackBarData ->
                        Snackbar(snackBarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                    }
                )
            }
        ) { innerPaddingModifier ->
            print(innerPaddingModifier.calculateBottomPadding().toString())
            NavHost(
                navController = appState.navController,
                startDestination = Graph.HOME
            ) {
                homeNavGraph(appState = appState)
            }
        }
    }
}

@Composable
fun rememberAppState(
    homeApps: List<Application>?,
    allApps: List<Application>?,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(homeApps, allApps,snackBarHostState, navController, snackBarManager, resources, coroutineScope) {
        LauncherAppState(
            snackBarHostState,
            navController,
            homeApps,
            allApps,
            snackBarManager,
            resources,
            coroutineScope
        )
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}