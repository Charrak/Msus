package de.einfachstart91.launcher.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import de.einfachstart91.launcher.data.PinMode
import de.einfachstart91.launcher.data.SettingsRepository
import de.einfachstart91.launcher.navigation.NavState
import de.einfachstart91.launcher.navigation.Screen
import de.einfachstart91.launcher.ui.screens.AdminScreen
import de.einfachstart91.launcher.ui.screens.ContactEditorScreen
import de.einfachstart91.launcher.ui.screens.EmergencyScreen
import de.einfachstart91.launcher.ui.screens.HelpScreen
import de.einfachstart91.launcher.ui.screens.HomeScreen
import de.einfachstart91.launcher.ui.screens.InfoScreen
import de.einfachstart91.launcher.ui.screens.MessagesScreen
import de.einfachstart91.launcher.ui.screens.PhoneScreen
import de.einfachstart91.launcher.ui.screens.PinScreen
import de.einfachstart91.launcher.ui.theme.LightBackground
import de.einfachstart91.launcher.util.rememberBatteryState
import de.einfachstart91.launcher.util.rememberCurrentTimeMillis
import kotlinx.coroutines.delay

@Composable
fun AppRoot(
    nav: NavState,
    repository: SettingsRepository,
    callPermissionGranted: Boolean,
    onRequestCallPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val settings by repository.state.collectAsState()
    val battery by rememberBatteryState()
    val timeMillis by rememberCurrentTimeMillis()
    val homeMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(homeMessage.value) {
        if (homeMessage.value != null) {
            delay(3_500)
            homeMessage.value = null
        }
    }

    val goBack: () -> Unit = { nav.back() }
    val homeBack: () -> Unit = {
        homeMessage.value = "Sie sind bereits auf dem Startbildschirm."
    }
    val showInfo: (String, String) -> Unit = { title, message ->
        nav.navigate(Screen.Info(title, message))
    }
    val openAdmin: () -> Unit = {
        nav.navigate(
            Screen.Pin(
                if (repository.hasPin()) PinMode.VERIFY else PinMode.SETUP,
            ),
        )
    }

    BackHandler(enabled = true) {
        if (nav.backStack.size > 1) nav.back() else homeBack()
    }

    Surface(modifier = modifier.fillMaxSize(), color = LightBackground) {
        when (val screen = nav.current) {
            Screen.Home -> HomeScreen(
                settings = settings,
                battery = battery,
                timeMillis = timeMillis,
                homeMessage = homeMessage.value,
                callPermissionGranted = callPermissionGranted,
                onBack = homeBack,
                onSettings = openAdmin,
                onInfo = showInfo,
                onNavigatePhone = { nav.navigate(Screen.Phone) },
                onNavigateMessages = { nav.navigate(Screen.Messages) },
                onNavigateHelp = { nav.navigate(Screen.Help) },
                onNavigateEmergency = { nav.navigate(Screen.Emergency) },
            )

            is Screen.Pin -> PinScreen(
                mode = screen.mode,
                repository = repository,
                onDone = {
                    when (screen.mode) {
                        PinMode.SETUP, PinMode.VERIFY -> nav.replaceTop(Screen.Admin)
                        PinMode.CHANGE -> nav.back()
                    }
                },
                onBack = goBack,
            )

            Screen.Admin -> AdminScreen(
                settings = settings,
                repository = repository,
                callPermissionGranted = callPermissionGranted,
                onRequestCallPermission = onRequestCallPermission,
                onBack = goBack,
                onOpenContactEditor = { id -> nav.navigate(Screen.ContactEditor(id)) },
                onChangePin = { nav.navigate(Screen.Pin(PinMode.CHANGE)) },
                onInfo = showInfo,
            )

            is Screen.ContactEditor -> ContactEditorScreen(
                contactId = screen.contactId,
                settings = settings,
                repository = repository,
                onBack = goBack,
                onInfo = showInfo,
            )

            Screen.Emergency -> EmergencyScreen(
                settings = settings,
                callPermissionGranted = callPermissionGranted,
                onBack = goBack,
                onInfo = showInfo,
                onOpenAdmin = openAdmin,
            )

            Screen.Help -> HelpScreen(
                settings = settings,
                callPermissionGranted = callPermissionGranted,
                onBack = goBack,
                onInfo = showInfo,
                onOpenAdmin = openAdmin,
            )

            Screen.Phone -> PhoneScreen(onBack = goBack, onInfo = showInfo)

            Screen.Messages -> MessagesScreen(onBack = goBack, onInfo = showInfo)

            is Screen.Info -> InfoScreen(
                title = screen.title,
                message = screen.message,
                onBack = goBack,
            )
        }
    }
}
