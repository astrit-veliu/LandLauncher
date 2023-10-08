package io.github.astrit_veliu.landlauncher.ui.presentation

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import io.github.astrit_veliu.landlauncher.ui.LauncherApp
import io.github.astrit_veliu.landlauncher.ui.launcher.LauncherViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val homeApps by viewModel.homeAppsState.collectAsState(null)
            val allApps by viewModel.allAppsState.collectAsState(null)
            LauncherApp(
                false,
                homeApps = homeApps,
                allApps = allApps,
                onBoardingComplete = { },
                onDarkModeSet = {}
            )
        }
    }

/*    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Handle key press events here
        Log.e("PressedKey", keyCode.toString())
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                // Handle volume up key press
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                // Handle volume down key press
                return true
            }
            // Add more cases for other keys as needed
        }
        return super.onKeyDown(keyCode, event)
    }*/
}