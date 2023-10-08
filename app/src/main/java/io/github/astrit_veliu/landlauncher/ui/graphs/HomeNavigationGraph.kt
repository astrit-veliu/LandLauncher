package io.github.astrit_veliu.landlauncher.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.github.astrit_veliu.landlauncher.ui.LauncherAppState
import io.github.astrit_veliu.landlauncher.ui.presentation.drawer.Drawer
import io.github.astrit_veliu.landlauncher.ui.presentation.onboarding.OnBoarding

fun NavGraphBuilder.homeNavGraph(
    showOnboarding: Boolean = false,
    appState: LauncherAppState
) {
    navigation(
        route = Graph.HOME,
        startDestination = if (showOnboarding) Screens.OnBoardingScreen.route else Screens.HomeScreen.route
    ) {

        composable(Screens.OnBoardingScreen.route) {
            OnBoarding(
                onFinish = {
                    appState.clearAndNavigate(Screens.HomeScreen.route)
                }
            )
        }

        composable(Screens.HomeScreen.route) {
            Drawer(appState)
        }
    }
}

/**
 * enum values that represent the screens in the app
 */
sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")

    object OnBoardingScreen : Screens("on_boarding_screen")
}