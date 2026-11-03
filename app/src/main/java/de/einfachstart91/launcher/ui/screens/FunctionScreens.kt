package de.einfachstart91.launcher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.einfachstart91.launcher.R
import de.einfachstart91.launcher.data.AppSettings
import de.einfachstart91.launcher.ui.components.BigButton
import de.einfachstart91.launcher.ui.components.BottomBackButton
import de.einfachstart91.launcher.ui.components.ContactInfoCard
import de.einfachstart91.launcher.ui.components.ThickDivider
import de.einfachstart91.launcher.ui.components.TopBar
import de.einfachstart91.launcher.ui.theme.BluePhone
import de.einfachstart91.launcher.ui.theme.DarkText
import de.einfachstart91.launcher.ui.theme.GreenMessages
import de.einfachstart91.launcher.ui.theme.LightBackground
import de.einfachstart91.launcher.ui.theme.OrangeImportant
import de.einfachstart91.launcher.ui.theme.RedEmergency
import de.einfachstart91.launcher.ui.theme.fs
import de.einfachstart91.launcher.util.openCallLog
import de.einfachstart91.launcher.util.openDefaultSmsApp
import de.einfachstart91.launcher.util.openDialer
import de.einfachstart91.launcher.util.openNewSms
import de.einfachstart91.launcher.util.placeCall

@Composable
private fun FunctionFrame(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        TopBar(title = title, onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            content()
        }
    }
}

@Composable
fun PhoneScreen(
    onBack: () -> Unit,
    onInfo: (String, String) -> Unit,
    onNavigateContacts: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val error: (String) -> Unit = { onInfo("Telefon", it) }
    FunctionFrame(title = "TELEFON", onBack = onBack, modifier = modifier) {
        Text(
            text = "Was möchten Sie tun?",
            fontSize = fs(26),
            fontWeight = FontWeight.Bold,
            color = DarkText,
        )
        BigButton(
            label = "ANRUFEN",
            color = BluePhone,
            onClick = { context.openDialer(error) },
            iconRes = R.drawable.ic_phone,
        )
        BigButton(
            label = "ANRUFLISTE",
            color = BluePhone,
            onClick = { context.openCallLog(error) },
            iconRes = R.drawable.ic_call_log,
        )
        BigButton(
            label = "KONTAKTE SUCHEN",
            color = BluePhone,
            onClick = onNavigateContacts,
            iconRes = R.drawable.ic_person,
        )
        ThickDivider()
        BottomBackButton(onBack)
    }
}

@Composable
fun MessagesScreen(
    onBack: () -> Unit,
    onInfo: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val error: (String) -> Unit = { onInfo("Nachrichten", it) }
    FunctionFrame(title = "NACHRICHTEN", onBack = onBack, modifier = modifier) {
        Text(
            text = "SMS lesen oder schreiben",
            fontSize = fs(26),
            fontWeight = FontWeight.Bold,
            color = DarkText,
        )
        BigButton(
            label = "NACHRICHTEN LESEN",
            color = GreenMessages,
            onClick = { context.openDefaultSmsApp(error) },
            iconRes = R.drawable.ic_message,
        )
        BigButton(
            label = "NEUE SMS SCHREIBEN",
            color = GreenMessages,
            onClick = { context.openNewSms(error) },
            iconRes = R.drawable.ic_write,
        )
        Text(
            text = "Die Nachrichten bleiben in der normalen SMS-App auf diesem Gerät.",
            fontSize = fs(20),
            color = DarkText,
        )
        ThickDivider()
        BottomBackButton(onBack)
    }
}

@Composable
fun InfoScreen(
    title: String,
    message: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FunctionFrame(title = title, onBack = onBack, modifier = modifier) {
        Text(
            text = message,
            fontSize = fs(24),
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        BottomBackButton(onBack)
    }
}

@Composable
fun EmergencyScreen(
    settings: AppSettings,
    callPermissionGranted: Boolean,
    onBack: () -> Unit,
    onInfo: (String, String) -> Unit,
    onOpenAdmin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val emergency = settings.contacts.firstOrNull { it.id == settings.emergencyContactId }
    val error: (String) -> Unit = { onInfo("Notfall", it) }

    FunctionFrame(title = "NOTFALL", onBack = onBack, modifier = modifier) {
        Text(
            text = "NOTFALL – HILFE RUFEN",
            fontSize = fs(30),
            fontWeight = FontWeight.Bold,
            color = RedEmergency,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        ContactInfoCard(
            contact = emergency,
            missingText = "Kein Notfallkontakt festgelegt",
        )
        if (emergency != null && emergency.phone.isNotBlank()) {
            BigButton(
                label = "NOTFALLKONTAKT ANRUFEN",
                color = RedEmergency,
                onClick = { context.placeCall(emergency.phone, callPermissionGranted, error) },
                iconRes = R.drawable.ic_phone,
            )
        } else {
            BigButton(
                label = "NOTFALLKONTAKT FESTLEGEN",
                color = OrangeImportant,
                onClick = onOpenAdmin,
                iconRes = R.drawable.ic_settings,
            )
        }
        if (settings.show112) {
            BigButton(
                label = "112 ANRUFEN",
                color = RedEmergency,
                onClick = { context.placeCall("112", callPermissionGranted, error) },
                iconRes = R.drawable.ic_emergency,
            )
        }
        Text(
            text = "Es wird nichts automatisch gewählt. Sie entscheiden selbst mit einer großen Taste.",
            fontSize = fs(20),
            color = DarkText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        ThickDivider()
        BottomBackButton(onBack)
    }
}

@Composable
fun HelpScreen(
    settings: AppSettings,
    callPermissionGranted: Boolean,
    onBack: () -> Unit,
    onInfo: (String, String) -> Unit,
    onOpenAdmin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val help = settings.contacts.firstOrNull { it.id == settings.helpContactId }
        ?: settings.contacts.firstOrNull { it.id == settings.emergencyContactId }
    val error: (String) -> Unit = { onInfo("Hilfe", it) }

    FunctionFrame(title = "HILFE", onBack = onBack, modifier = modifier) {
        Text(
            text = "Einen festgelegten Angehörigen anrufen",
            fontSize = fs(26),
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        ContactInfoCard(
            contact = help,
            missingText = "Kein Hilfe-Kontakt festgelegt",
        )
        if (help != null && help.phone.isNotBlank()) {
            BigButton(
                label = "JETZT ANRUFEN",
                color = BluePhone,
                onClick = { context.placeCall(help.phone, callPermissionGranted, error) },
                iconRes = R.drawable.ic_phone,
            )
        } else {
            BigButton(
                label = "HILFE-KONTAKT FESTLEGEN",
                color = OrangeImportant,
                onClick = onOpenAdmin,
                iconRes = R.drawable.ic_settings,
            )
        }
        Text(
            text = "Bei einem echten Notfall bitte die rote NOTFALL-Taste benutzen.",
            fontSize = fs(20),
            color = DarkText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        ThickDivider()
        BottomBackButton(onBack)
    }
}
