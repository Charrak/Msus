package de.einfachstart91.launcher.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import de.einfachstart91.launcher.R
import de.einfachstart91.launcher.data.AppSettings
import de.einfachstart91.launcher.data.ContactPickerMode
import de.einfachstart91.launcher.data.SeniorContact
import de.einfachstart91.launcher.data.SettingsRepository
import de.einfachstart91.launcher.ui.components.BigButton
import de.einfachstart91.launcher.ui.components.BigTextField
import de.einfachstart91.launcher.ui.components.BottomBackButton
import de.einfachstart91.launcher.ui.components.ContactAvatar
import de.einfachstart91.launcher.ui.components.TopBar
import de.einfachstart91.launcher.ui.theme.BluePhone
import de.einfachstart91.launcher.ui.theme.CardWhite
import de.einfachstart91.launcher.ui.theme.DarkText
import de.einfachstart91.launcher.ui.theme.LightBackground
import de.einfachstart91.launcher.ui.theme.SoftBlue
import de.einfachstart91.launcher.ui.theme.fs
import de.einfachstart91.launcher.util.DeviceContact
import de.einfachstart91.launcher.util.hasContactsPermission
import de.einfachstart91.launcher.util.loadDeviceContacts
import de.einfachstart91.launcher.util.placeCall
import de.einfachstart91.launcher.util.rememberContactPhoto
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ContactPickerScreen(
    mode: ContactPickerMode,
    targetId: String?,
    settings: AppSettings,
    repository: SettingsRepository,
    callPermissionGranted: Boolean,
    onBack: () -> Unit,
    onInfo: (String, String) -> Unit,
    onPicked: (DeviceContact) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var permissionGranted by remember { mutableStateOf(context.hasContactsPermission()) }
    var query by remember { mutableStateOf("") }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted -> permissionGranted = granted }

    LaunchedEffect(Unit) {
        if (!context.hasContactsPermission()) {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    val contacts by produceState(emptyList<DeviceContact>(), permissionGranted) {
        value = if (permissionGranted) context.loadDeviceContacts() else emptyList()
    }

    val filtered = contacts.filter { contact ->
        query.isBlank() ||
            contact.name.contains(query, ignoreCase = true) ||
            contact.number.contains(query, ignoreCase = true) ||
            contact.number.filter { it.isDigit() }.contains(query.filter { it.isDigit() })
    }

    fun select(contact: DeviceContact) {
        when (mode) {
            ContactPickerMode.ADD -> {
                if (settings.contacts.size >= SettingsRepository.MAX_CONTACTS) {
                    onInfo("Zu viele Kontakte", "Es können höchstens 8 Kontakte gespeichert werden.")
                    return
                }
                val id = UUID.randomUUID().toString()
                scope.launch {
                    val path = contact.photoUri?.let { repository.copyContactPhoto(Uri.parse(it), id) }
                    repository.addOrUpdateContact(
                        SeniorContact(
                            id = id,
                            name = contact.name,
                            relation = "",
                            phone = contact.number,
                            photoPath = path,
                            favorite = true,
                            order = settings.contacts.size,
                        ),
                    )
                    onDone()
                }
            }

            ContactPickerMode.CALL -> context.placeCall(
                contact.number,
                callPermissionGranted,
            ) { message -> onInfo("Anruf", message) }

            ContactPickerMode.FILL -> {
                onPicked(contact)
                onDone()
            }
        }
    }

    val title = when (mode) {
        ContactPickerMode.ADD -> "KONTAKT HINZUFÜGEN"
        ContactPickerMode.CALL -> "KONTAKT ANRUFEN"
        ContactPickerMode.FILL -> "AUS KONTAKTLISTE WÄHLEN"
    }
    val hint = when (mode) {
        ContactPickerMode.ADD -> "Alle Kontakte dieses Geräts durchsuchen und als Lieblingskontakt übernehmen."
        ContactPickerMode.CALL -> "Alle Kontakte dieses Geräts durchsuchen und direkt anrufen."
        ContactPickerMode.FILL -> "Name, Telefonnummer und Foto aus der Kontaktliste übernehmen."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        TopBar(title = title, onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = hint,
                fontSize = fs(20),
                color = DarkText,
            )
            if (!permissionGranted) {
                Text(
                    text = "EinfachStart 91 benötigt Zugriff auf die Kontaktliste, damit Sie Kontakte auswählen können. Die Daten bleiben auf diesem Gerät.",
                    fontSize = fs(22),
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                )
                BigButton(
                    label = "ZUGRIFF ERLAUBEN",
                    color = BluePhone,
                    onClick = { permissionLauncher.launch(Manifest.permission.READ_CONTACTS) },
                    iconRes = R.drawable.ic_person,
                )
                Spacer(Modifier.weight(1f))
            } else {
                BigTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = "ALLE KONTAKTE DURCHSUCHEN",
                    keyboardType = KeyboardType.Text,
                )
                Text(
                    text = "${filtered.size} Kontakte gefunden",
                    fontSize = fs(18),
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                )
                if (filtered.isEmpty()) {
                    Text(
                        text = "Keine passenden Kontakte gefunden. Sie können den Suchbegriff ändern oder einen Kontakt manuell in der Verwaltung anlegen.",
                        fontSize = fs(20),
                        color = DarkText,
                    )
                    Spacer(Modifier.weight(1f))
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(filtered, key = { "${it.id}|${it.number}" }) { contact ->
                            DeviceContactRow(
                                contact = contact,
                                mode = mode,
                                onClick = { select(contact) },
                            )
                        }
                    }
                }
            }
            BottomBackButton(onBack)
        }
    }
}

@Composable
private fun DeviceContactRow(
    contact: DeviceContact,
    mode: ContactPickerMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val photo by rememberContactPhoto(contact.photoUri)
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val image = photo
            if (image != null) {
                Image(
                    bitmap = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(SoftBlue),
                )
            } else {
                ContactAvatar(
                    SeniorContact(name = contact.name),
                    Modifier.size(72.dp),
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    fontSize = fs(22),
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    maxLines = 2,
                )
                Text(
                    text = contact.number,
                    fontSize = fs(18),
                    color = DarkText,
                    maxLines = 1,
                )
            }
            Image(
                painter = painterResource(
                    if (mode == ContactPickerMode.CALL) R.drawable.ic_phone else R.drawable.ic_add,
                ),
                contentDescription = null,
                colorFilter = ColorFilter.tint(BluePhone),
                modifier = Modifier.size(36.dp),
            )
        }
    }
}
