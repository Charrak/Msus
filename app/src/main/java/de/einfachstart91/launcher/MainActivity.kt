package de.einfachstart91.launcher

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.einfachstart91.launcher.data.SettingsRepository
import de.einfachstart91.launcher.navigation.NavState
import de.einfachstart91.launcher.ui.AppRoot
import de.einfachstart91.launcher.ui.theme.EinfachStartTheme
import de.einfachstart91.launcher.util.hasCallPermission
import de.einfachstart91.launcher.util.setupLightSystemBars

class MainActivity : ComponentActivity() {
    private val navState = NavState()
    private lateinit var repository: SettingsRepository
    private var callPermissionGranted by mutableStateOf(false)
    private lateinit var requestCallPermission: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLightSystemBars()
        repository = SettingsRepository(applicationContext)

        requestCallPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                callPermissionGranted = granted
            }
        callPermissionGranted = hasCallPermission()
        if (!callPermissionGranted) {
            requestCallPermission.launch(Manifest.permission.CALL_PHONE)
        }

        setContent {
            val settings by repository.state.collectAsState()
            EinfachStartTheme(fontScale = settings.fontScale) {
                AppRoot(
                    nav = navState,
                    repository = repository,
                    callPermissionGranted = callPermissionGranted,
                    onRequestCallPermission = {
                        requestCallPermission.launch(Manifest.permission.CALL_PHONE)
                    },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == Intent.ACTION_MAIN && intent.hasCategory(Intent.CATEGORY_HOME)) {
            navState.reset()
        }
    }
}
