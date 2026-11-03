package de.einfachstart91.launcher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.einfachstart91.launcher.R
import de.einfachstart91.launcher.data.AppSettings
import de.einfachstart91.launcher.data.FunctionKeys
import de.einfachstart91.launcher.data.SeniorContact
import de.einfachstart91.launcher.ui.components.BatteryBadge
import de.einfachstart91.launcher.ui.components.BigButton
import de.einfachstart91.launcher.ui.components.ContactCard
import de.einfachstart91.launcher.ui.components.ThickDivider
import de.einfachstart91.launcher.ui.components.TopBar
import de.einfachstart91.launcher.ui.theme.BluePhone
import de.einfachstart91.launcher.ui.theme.DarkText
import de.einfachstart91.launcher.ui.theme.GreenMessages
import de.einfachstart91.launcher.ui.theme.LightBackground
import de.einfachstart91.launcher.ui.theme.OrangeImportant
import de.einfachstart91.launcher.ui.theme.RedEmergency
import de.einfachstart91.launcher.ui.theme.fs
import de.einfachstart91.launcher.util.BatteryState
import de.einfachstart91.launcher.util.formatClock
import de.einfachstart91.launcher.util.formatDate
import de.einfachstart91.launcher.util.openCamera
import de.einfachstart91.launcher.util.openPhotos
import de.einfachstart91.launcher.util.openWhatsApp
import de.einfachstart91.launcher.util.placeCall

private data class HomeFunction(
    val key: String,
    val label: String,
    val iconRes: Int,
    val color: Color,
    val onClick: () -> Unit,
)

@Composable
fun HomeScreen(
    settings: AppSettings,
    battery: BatteryState,
    timeMillis: Long,
    homeMessage: String?,
    callPermissionGranted: Boolean,
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onInfo: (String, String) -> Unit,
    onNavigatePhone: () -> Unit,
    onNavigateMessages: () -> Unit,
    onNavigateHelp: () -> Unit,
    onNavigateEmergency: () -> Unit,
    onNavigateContacts: () -> Unit,
    onAddContact: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val showError: (String) -> Unit = { message -> onInfo("Hinweis", message) }

    val onCall: (SeniorContact) -> Unit = { contact ->
        if (contact.phone.isBlank()) {
            onInfo(
                "Keine Telefonnummer",
                "Für ${contact.name.ifBlank { "diesen Kontakt" }} ist noch keine Nummer hinterlegt. Bitte die Einstellungen öffnen.",
            )
        } else {
            context.placeCall(contact.phone, callPermissionGranted, showError)
        }
    }

    fun function(
        key: String,
        label: String,
        iconRes: Int,
        color: Color,
        onClick: () -> Unit,
    ): HomeFunction? = if (settings.visibleFunctions[key] != false) {
        HomeFunction(key, label, iconRes, color, onClick)
    } else {
        null
    }

    val functions: List<HomeFunction?> = listOf(
        function(FunctionKeys.PHONE, "TELEFON", R.drawable.ic_phone, BluePhone, onNavigatePhone),
        function(FunctionKeys.CONTACTS, "KONTAKTE", R.drawable.ic_person, BluePhone, onNavigateContacts),
        function(FunctionKeys.MESSAGES, "NACHRICHTEN", R.drawable.ic_message, GreenMessages, onNavigateMessages),
        function(FunctionKeys.WHATSAPP, "WHATSAPP", R.drawable.ic_whatsapp, GreenMessages) {
            context.openWhatsApp(showError)
        },
        function(FunctionKeys.PHOTOS, "FOTOS", R.drawable.ic_photos, OrangeImportant) {
            context.openPhotos(showError)
        },
        function(FunctionKeys.CAMERA, "KAMERA", R.drawable.ic_camera, OrangeImportant) {
            context.openCamera(showError)
        },
        function(FunctionKeys.HELP, "HILFE", R.drawable.ic_help, BluePhone, onNavigateHelp),
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        val compact = maxHeight < 780.dp
        val scrollModifier = if (compact) Modifier.verticalScroll(rememberScrollState()) else Modifier

        Column(Modifier.fillMaxSize().then(scrollModifier)) {
            TopBar(
                title = "EINFACHSTART 91",
                onBack = onBack,
                onSettings = onSettings,
                modifier = Modifier.height(80.dp),
            )

            if (compact) {
                HomeHeader(
                    timeMillis = timeMillis,
                    battery = battery,
                    weatherText = settings.weatherText,
                    homeMessage = homeMessage,
                    modifier = Modifier.height(120.dp),
                )
                HomeSectionLabel(Modifier.height(32.dp))
                ContactsFixed(
                    contacts = settings.contacts,
                    onCall = onCall,
                    onAdd = onAddContact,
                    modifier = Modifier.fillMaxWidth(),
                )
                ThickDivider(Modifier.height(8.dp))
                FunctionsFixed(functions, Modifier.height(354.dp))
                EmergencyBar(onNavigateEmergency, Modifier.height(88.dp))
            } else {
                HomeHeader(
                    timeMillis = timeMillis,
                    battery = battery,
                    weatherText = settings.weatherText,
                    homeMessage = homeMessage,
                    modifier = Modifier.height(100.dp),
                )
                HomeSectionLabel(Modifier.height(28.dp))
                ContactsGrid(
                    contacts = settings.contacts,
                    onCall = onCall,
                    onAdd = onAddContact,
                    modifier = Modifier.weight(1f),
                )
                ThickDivider(Modifier.height(8.dp))
                FunctionsGrid(functions, Modifier.height(336.dp))
                EmergencyBar(onNavigateEmergency, Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun HomeHeader(
    timeMillis: Long,
    battery: BatteryState,
    weatherText: String,
    homeMessage: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = "${formatClock(timeMillis)} Uhr",
                fontSize = fs(50),
                fontWeight = FontWeight.Bold,
                color = DarkText,
                maxLines = 1,
                lineHeight = fs(52),
            )
            Text(
                text = formatDate(timeMillis),
                fontSize = fs(17),
                color = DarkText,
                maxLines = 1,
            )
            if (weatherText.isNotBlank()) {
                Text(
                    text = weatherText,
                    fontSize = fs(16),
                    color = DarkText,
                    maxLines = 1,
                )
            }
            if (homeMessage != null) {
                Text(
                    text = homeMessage,
                    fontSize = fs(17),
                    fontWeight = FontWeight.Bold,
                    color = BluePhone,
                    maxLines = 2,
                )
            }
        }
        BatteryBadge(battery)
    }
}

@Composable
private fun HomeSectionLabel(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = "MEINE LIEBSTEN",
            fontSize = fs(20),
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ContactsGrid(
    contacts: List<SeniorContact>,
    onCall: (SeniorContact) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val favorites = contacts.filter { it.favorite }.take(8)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        for (row in 0 until 4) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (column in 0 until 2) {
                    val contact = favorites.getOrNull(row * 2 + column)
                    ContactCard(
                        contact = contact,
                        onClick = { if (contact != null) onCall(contact) else onAdd() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 2.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactsFixed(
    contacts: List<SeniorContact>,
    onCall: (SeniorContact) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val favorites = contacts.filter { it.favorite }.take(8)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        for (row in 0 until 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (column in 0 until 2) {
                    val contact = favorites.getOrNull(row * 2 + column)
                    ContactCard(
                        contact = contact,
                        onClick = { if (contact != null) onCall(contact) else onAdd() },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun FunctionsGrid(functions: List<HomeFunction?>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        for (row in 0 until 4) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (column in 0 until 2) {
                    val item = functions.getOrNull(row * 2 + column)
                    if (item != null) {
                        BigButton(
                            label = item.label,
                            color = item.color,
                            onClick = item.onClick,
                            iconRes = item.iconRes,
                            minHeight = 80.dp,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun FunctionsFixed(functions: List<HomeFunction?>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        for (row in 0 until 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (column in 0 until 2) {
                    val item = functions.getOrNull(row * 2 + column)
                    if (item != null) {
                        BigButton(
                            label = item.label,
                            color = item.color,
                            onClick = item.onClick,
                            iconRes = item.iconRes,
                            minHeight = 80.dp,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmergencyBar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    BigButton(
        label = "NOTFALL – HILFE RUFEN",
        color = RedEmergency,
        onClick = onClick,
        iconRes = R.drawable.ic_emergency,
        minHeight = 80.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    )
}
