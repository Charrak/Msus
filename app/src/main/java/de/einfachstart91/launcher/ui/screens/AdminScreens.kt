package de.einfachstart91.launcher.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.einfachstart91.launcher.R
import de.einfachstart91.launcher.data.AppSettings
import de.einfachstart91.launcher.data.ContactPickerMode
import de.einfachstart91.launcher.data.FunctionKeys
import de.einfachstart91.launcher.data.PinMode
import de.einfachstart91.launcher.data.SeniorContact
import de.einfachstart91.launcher.data.SettingsRepository
import de.einfachstart91.launcher.ui.components.BigButton
import de.einfachstart91.launcher.ui.components.BigConfirmDialog
import de.einfachstart91.launcher.ui.components.BigIconButton
import de.einfachstart91.launcher.ui.components.BigSwitchRow
import de.einfachstart91.launcher.ui.components.BigTextField
import de.einfachstart91.launcher.ui.components.BottomBackButton
import de.einfachstart91.launcher.ui.components.ContactAvatar
import de.einfachstart91.launcher.ui.components.SectionHeading
import de.einfachstart91.launcher.ui.components.ThickDivider
import de.einfachstart91.launcher.ui.components.TopBar
import de.einfachstart91.launcher.ui.theme.BluePhone
import de.einfachstart91.launcher.ui.theme.CardWhite
import de.einfachstart91.launcher.ui.theme.DarkText
import de.einfachstart91.launcher.ui.theme.GreenMessages
import de.einfachstart91.launcher.ui.theme.LightBackground
import de.einfachstart91.launcher.ui.theme.NeutralButton
import de.einfachstart91.launcher.ui.theme.OrangeImportant
import de.einfachstart91.launcher.ui.theme.RedEmergency
import de.einfachstart91.launcher.ui.theme.fs
import de.einfachstart91.launcher.util.DeviceContact
import de.einfachstart91.launcher.util.isDefaultHomeRole
import de.einfachstart91.launcher.util.openHomeSettings
import de.einfachstart91.launcher.util.openSoundSettings
import de.einfachstart91.launcher.util.openWifiSettings
import de.einfachstart91.launcher.util.rememberBatteryState
import de.einfachstart91.launcher.util.rememberWifiEnabled
import de.einfachstart91.launcher.util.requestDefaultHomeRole
import kotlinx.coroutines.launch
import java.util.UUID

private val functionLabels = mapOf(
    FunctionKeys.PHONE to "TELEFON",
    FunctionKeys.MESSAGES to "NACHRICHTEN",
    FunctionKeys.WHATSAPP to "WHATSAPP",
    FunctionKeys.PHOTOS to "FOTOS",
    FunctionKeys.CAMERA to "KAMERA",
    FunctionKeys.HELP to "HILFE",
    FunctionKeys.CONTACTS to "KONTAKTE",
)

private fun iconForFunction(key: String): Int = when (key) {
    FunctionKeys.PHONE -> R.drawable.ic_phone
    FunctionKeys.MESSAGES -> R.drawable.ic_message
    FunctionKeys.WHATSAPP -> R.drawable.ic_whatsapp
    FunctionKeys.PHOTOS -> R.drawable.ic_photos
    FunctionKeys.CAMERA -> R.drawable.ic_camera
    FunctionKeys.CONTACTS -> R.drawable.ic_person
    else -> R.drawable.ic_help
}

@Composable
fun AdminScreen(
    settings: AppSettings,
    repository: SettingsRepository,
    callPermissionGranted: Boolean,
    onRequestCallPermission: () -> Unit,
    onBack: () -> Unit,
    onOpenContactEditor: (String?) -> Unit,
    onOpenContactPicker: (ContactPickerMode) -> Unit,
    onChangePin: () -> Unit,
    onInfo: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val battery by rememberBatteryState()
    val wifi by rememberWifiEnabled()
    val error: (String) -> Unit = { onInfo("Einstellungen", it) }
    var deleteTarget by remember { mutableStateOf<SeniorContact?>(null) }
    var weather by remember(settings.weatherText) { mutableStateOf(settings.weatherText) }
    val isDefaultHome = context.isDefaultHomeRole()

    deleteTarget?.let { target ->
        BigConfirmDialog(
            title = "Kontakt löschen?",
            message = "${target.name.ifBlank { "Dieser Kontakt" }} wirklich löschen? Das Foto wird ebenfalls vom Gerät entfernt.",
            confirmLabel = "LÖSCHEN",
            onConfirm = {
                repository.deleteContact(target.id)
                deleteTarget = null
            },
            onDismiss = { deleteTarget = null },
            destructive = true,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        TopBar(title = "EINSTELLUNGEN", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Verwaltung für Angehörige",
                fontSize = fs(28),
                fontWeight = FontWeight.Bold,
                color = DarkText,
            )

            SectionHeading("Kontakte")
            Text(
                text = "Bis zu 8 Personen. Nur Lieblingskontakte erscheinen auf dem Startbildschirm.",
                fontSize = fs(18),
                color = DarkText,
            )
            settings.contacts.forEach { contact ->
                val index = settings.contacts.indexOf(contact)
                AdminContactRow(
                    contact = contact,
                    canUp = index > 0,
                    canDown = index < settings.contacts.lastIndex,
                    onUp = { repository.moveContact(contact.id, -1) },
                    onDown = { repository.moveContact(contact.id, 1) },
                    onEdit = { onOpenContactEditor(contact.id) },
                    onDelete = { deleteTarget = contact },
                    onToggleFavorite = { repository.toggleFavorite(contact.id) },
                )
            }
            BigSwitchRow(
                label = "Kontakte ohne PIN hinzufügen erlauben",
                checked = settings.allowAddWithoutPin,
                onCheckedChange = { repository.setAllowAddWithoutPin(it) },
                iconRes = R.drawable.ic_person,
            )
            BigButton(
                label = "KONTAKT HINZUFÜGEN",
                color = BluePhone,
                onClick = { onOpenContactEditor(null) },
                iconRes = R.drawable.ic_add,
                enabled = settings.contacts.size < SettingsRepository.MAX_CONTACTS,
            )
            BigButton(
                label = "AUS KONTAKTLISTE HINZUFÜGEN",
                color = BluePhone,
                onClick = { onOpenContactPicker(ContactPickerMode.ADD) },
                iconRes = R.drawable.ic_person,
                enabled = settings.contacts.size < SettingsRepository.MAX_CONTACTS,
            )
            if (settings.contacts.size >= SettingsRepository.MAX_CONTACTS) {
                Text(
                    text = "Maximal 8 Kontakte. Bitte zuerst einen Kontakt löschen.",
                    fontSize = fs(18),
                    color = DarkText,
                )
            }

            SectionHeading("Notfall und Hilfe")
            Text("Notfallkontakt", fontSize = fs(22), fontWeight = FontWeight.Bold, color = DarkText)
            settings.contacts.forEach { contact ->
                val selected = settings.emergencyContactId == contact.id
                BigButton(
                    label = contact.name.ifBlank { "Kontakt" },
                    color = if (selected) RedEmergency else BluePhone,
                    onClick = { repository.setEmergencyContact(contact.id) },
                    iconRes = if (selected) R.drawable.ic_check else R.drawable.ic_person,
                    minHeight = 80.dp,
                )
            }
            BigButton(
                label = "KEIN NOTFALLKONTAKT",
                color = NeutralButton,
                onClick = { repository.setEmergencyContact(null) },
                minHeight = 80.dp,
            )
            Text("Hilfe-Kontakt", fontSize = fs(22), fontWeight = FontWeight.Bold, color = DarkText)
            settings.contacts.forEach { contact ->
                val selected = settings.helpContactId == contact.id
                BigButton(
                    label = contact.name.ifBlank { "Kontakt" },
                    color = if (selected) OrangeImportant else BluePhone,
                    onClick = { repository.setHelpContact(contact.id) },
                    iconRes = if (selected) R.drawable.ic_check else R.drawable.ic_help,
                    minHeight = 80.dp,
                )
            }
            BigButton(
                label = "KEIN HILFE-KONTAKT",
                color = NeutralButton,
                onClick = { repository.setHelpContact(null) },
                minHeight = 80.dp,
            )
            BigSwitchRow(
                label = "Notrufnummer 112 anzeigen",
                checked = settings.show112,
                onCheckedChange = { repository.setShow112(it) },
                iconRes = R.drawable.ic_emergency,
            )

            SectionHeading("Schrift und Anzeige")
            Text("Schriftgröße", fontSize = fs(22), fontWeight = FontWeight.Bold, color = DarkText)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BigButton(
                    label = "NORMAL",
                    color = if (settings.fontScale <= 1.05f) BluePhone else NeutralButton,
                    onClick = { repository.setFontScale(1.0f) },
                    minHeight = 80.dp,
                    modifier = Modifier.weight(1f),
                )
                BigButton(
                    label = "GROSS",
                    color = if (settings.fontScale > 1.05f && settings.fontScale < 1.25f) BluePhone else NeutralButton,
                    onClick = { repository.setFontScale(1.15f) },
                    minHeight = 80.dp,
                    modifier = Modifier.weight(1f),
                )
                BigButton(
                    label = "SEHR GROSS",
                    color = if (settings.fontScale >= 1.25f) BluePhone else NeutralButton,
                    onClick = { repository.setFontScale(1.30f) },
                    minHeight = 80.dp,
                    modifier = Modifier.weight(1f),
                )
            }
            BigTextField(
                value = weather,
                onValueChange = {
                    weather = it
                    repository.setWeatherText(it)
                },
                label = "Wetterhinweis (optional, nur lokal gespeichert)",
            )

            SectionHeading("Funktionen auf dem Startbildschirm")
            FunctionKeys.ALL.forEach { key ->
                BigSwitchRow(
                    label = functionLabels[key] ?: key,
                    checked = settings.visibleFunctions[key] != false,
                    onCheckedChange = { repository.setFunctionVisible(key, it) },
                    iconRes = iconForFunction(key),
                )
            }

            SectionHeading("Gerät")
            Text(
                text = "Akku: ${battery.percent} %${if (battery.charging) ", lädt" else ""} · WLAN: ${if (wifi) "an" else "aus"}",
                fontSize = fs(20),
                color = DarkText,
            )
            BigButton(
                label = "LAUTSTÄRKE UND KLINGELTON",
                color = OrangeImportant,
                onClick = { context.openSoundSettings(error) },
                iconRes = R.drawable.ic_volume,
            )
            BigButton(
                label = "WLAN-EINSTELLUNGEN",
                color = OrangeImportant,
                onClick = { context.openWifiSettings(error) },
                iconRes = R.drawable.ic_wifi,
            )
            Text(
                text = if (isDefaultHome) {
                    "EinfachStart 91 ist bereits der Standard-Startbildschirm."
                } else {
                    "EinfachStart 91 ist noch nicht als Standard-Startbildschirm festgelegt."
                },
                fontSize = fs(20),
                color = DarkText,
            )
            BigButton(
                label = "ALS STANDARD-LAUNCHER FESTLEGEN",
                color = BluePhone,
                onClick = { context.requestDefaultHomeRole(error) },
                iconRes = R.drawable.ic_home,
            )
            BigButton(
                label = "STARTBILDSCHIRM-EINSTELLUNGEN ÖFFNEN",
                color = NeutralButton,
                onClick = { context.openHomeSettings(error) },
                iconRes = R.drawable.ic_settings,
            )
            if (!callPermissionGranted) {
                BigButton(
                    label = "ANRUFBERECHTIGUNG ERNEUT ANFRAGEN",
                    color = OrangeImportant,
                    onClick = onRequestCallPermission,
                    iconRes = R.drawable.ic_phone,
                )
            }

            SectionHeading("PIN")
            BigButton(
                label = "PIN ÄNDERN",
                color = NeutralButton,
                onClick = onChangePin,
                iconRes = R.drawable.ic_lock,
            )

            ThickDivider()
            BottomBackButton(onBack)
        }
    }
}

@Composable
private fun AdminContactRow(
    contact: SeniorContact,
    canUp: Boolean,
    canDown: Boolean,
    onUp: () -> Unit,
    onDown: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ContactAvatar(contact, Modifier.size(64.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = contact.name.ifBlank { "Name fehlt" },
                        fontSize = fs(22),
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                    )
                    Text(
                        text = contact.phone.ifBlank { "Keine Nummer" },
                        fontSize = fs(18),
                        color = DarkText,
                    )
                }
                BigIconButton(
                    iconRes = if (contact.favorite) R.drawable.ic_favorite else R.drawable.ic_person,
                    accessibilityLabel = if (contact.favorite) {
                        "Lieblingskontakt, zum Entfernen antippen"
                    } else {
                        "Als Lieblingskontakt setzen"
                    },
                    color = if (contact.favorite) OrangeImportant else NeutralButton,
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(80.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BigIconButton(
                    iconRes = R.drawable.ic_arrow_up,
                    accessibilityLabel = "Kontakt nach oben sortieren",
                    color = BluePhone,
                    onClick = onUp,
                    modifier = Modifier.size(80.dp),
                    enabled = canUp,
                )
                BigIconButton(
                    iconRes = R.drawable.ic_arrow_down,
                    accessibilityLabel = "Kontakt nach unten sortieren",
                    color = BluePhone,
                    onClick = onDown,
                    modifier = Modifier.size(80.dp),
                    enabled = canDown,
                )
                BigIconButton(
                    iconRes = R.drawable.ic_edit,
                    accessibilityLabel = "Kontakt bearbeiten",
                    color = GreenMessages,
                    onClick = onEdit,
                    modifier = Modifier.size(80.dp),
                )
                BigIconButton(
                    iconRes = R.drawable.ic_delete,
                    accessibilityLabel = "Kontakt löschen",
                    color = RedEmergency,
                    onClick = onDelete,
                    modifier = Modifier.size(80.dp),
                )
            }
        }
    }
}

@Composable
fun ContactEditorScreen(
    contactId: String?,
    settings: AppSettings,
    repository: SettingsRepository,
    pickedContact: DeviceContact?,
    onPickedContactConsumed: () -> Unit,
    onPickFromContacts: () -> Unit,
    onBack: () -> Unit,
    onInfo: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val existing = settings.contacts.firstOrNull { it.id == contactId }
    val id = remember(contactId) { contactId ?: UUID.randomUUID().toString() }
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var relation by remember { mutableStateOf(existing?.relation ?: "") }
    var phone by remember { mutableStateOf(existing?.phone ?: "") }
    var photoPath by remember { mutableStateOf(existing?.photoPath) }
    var favorite by remember { mutableStateOf(existing?.favorite ?: true) }
    var asEmergency by remember { mutableStateOf(settings.emergencyContactId == id) }
    var asHelp by remember { mutableStateOf(settings.helpContactId == id) }
    var showDelete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val canSave = existing != null || settings.contacts.size < SettingsRepository.MAX_CONTACTS

    LaunchedEffect(pickedContact) {
        val picked = pickedContact ?: return@LaunchedEffect
        name = picked.name
        phone = picked.number
        photoPath = picked.photoUri?.let { repository.copyContactPhoto(Uri.parse(it), id) }
        onPickedContactConsumed()
    }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val path = repository.copyContactPhoto(uri, id)
            if (path != null) {
                photoPath = path
            } else {
                onInfo("Foto", "Das Bild konnte nicht auf dem Gerät gespeichert werden.")
            }
        }
    }

    fun save() {
        if (name.isBlank() || phone.isBlank()) {
            onInfo("Fehlende Angaben", "Bitte mindestens Name und Telefonnummer eingeben.")
            return
        }
        if (!canSave) {
            onInfo("Zu viele Kontakte", "Es können höchstens 8 Kontakte gespeichert werden.")
            return
        }
        val contact = SeniorContact(
            id = id,
            name = name.trim(),
            relation = relation.trim(),
            phone = phone.trim(),
            photoPath = photoPath,
            favorite = favorite,
            order = existing?.order ?: settings.contacts.size,
        )
        repository.addOrUpdateContact(contact)
        if (asEmergency) {
            repository.setEmergencyContact(id)
        } else if (settings.emergencyContactId == id) {
            repository.setEmergencyContact(null)
        }
        if (asHelp) {
            repository.setHelpContact(id)
        } else if (settings.helpContactId == id) {
            repository.setHelpContact(null)
        }
        onBack()
    }

    if (showDelete) {
        BigConfirmDialog(
            title = "Kontakt löschen?",
            message = "Diesen Kontakt und sein Foto endgültig vom Gerät entfernen?",
            confirmLabel = "LÖSCHEN",
            onConfirm = {
                repository.deleteContact(id)
                onBack()
            },
            onDismiss = { showDelete = false },
            destructive = true,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        TopBar(
            title = if (existing == null) "NEUER KONTAKT" else "KONTAKT BEARBEITEN",
            onBack = onBack,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val preview = SeniorContact(
                id = id,
                name = name,
                relation = relation,
                phone = phone,
                photoPath = photoPath,
                favorite = favorite,
                order = 0,
            )
            ContactAvatar(preview, Modifier.size(120.dp))
            BigButton(
                label = "FOTO AUS GALERIE WÄHLEN",
                color = BluePhone,
                onClick = { photoPicker.launch("image/*") },
                iconRes = R.drawable.ic_photos,
            )
            BigButton(
                label = "AUS KONTAKTLISTE ÜBERNEHMEN",
                color = BluePhone,
                onClick = onPickFromContacts,
                iconRes = R.drawable.ic_person,
            )
            BigTextField(
                value = name,
                onValueChange = { name = it },
                label = "Vollständiger Name",
            )
            BigTextField(
                value = relation,
                onValueChange = { relation = it },
                label = "Beziehung, zum Beispiel Sohn",
            )
            BigTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Telefonnummer",
                keyboardType = KeyboardType.Phone,
            )
            BigSwitchRow(
                label = "Lieblingskontakt (auf dem Startbildschirm zeigen)",
                checked = favorite,
                onCheckedChange = { favorite = it },
                iconRes = R.drawable.ic_favorite,
            )
            BigSwitchRow(
                label = "Als Notfallkontakt setzen",
                checked = asEmergency,
                onCheckedChange = { asEmergency = it },
                iconRes = R.drawable.ic_emergency,
            )
            BigSwitchRow(
                label = "Als Hilfe-Kontakt setzen",
                checked = asHelp,
                onCheckedChange = { asHelp = it },
                iconRes = R.drawable.ic_help,
            )
            if (!canSave) {
                Text(
                    text = "Maximal 8 Kontakte. Bitte zuerst einen Kontakt löschen.",
                    fontSize = fs(18),
                    color = RedEmergency,
                )
            }
            BigButton(
                label = "SPEICHERN",
                color = GreenMessages,
                onClick = { save() },
                iconRes = R.drawable.ic_check,
                enabled = canSave,
            )
            if (existing != null) {
                BigButton(
                    label = "KONTAKT LÖSCHEN",
                    color = RedEmergency,
                    onClick = { showDelete = true },
                    iconRes = R.drawable.ic_delete,
                )
            }
            ThickDivider()
            BottomBackButton(onBack)
        }
    }
}

@Composable
fun PinScreen(
    mode: PinMode,
    repository: SettingsRepository,
    onDone: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var entry by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(0) }
    var firstNew by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val title = when (mode) {
        PinMode.SETUP -> if (step == 0) "Neue PIN eingeben" else "Neue PIN wiederholen"
        PinMode.VERIFY -> "PIN eingeben"
        PinMode.CHANGE -> when (step) {
            0 -> "Alte PIN eingeben"
            1 -> "Neue PIN eingeben"
            else -> "Neue PIN wiederholen"
        }
    }

    fun appendDigit(digit: String) {
        if (entry.length < 8) {
            entry += digit
            error = null
        }
    }

    fun submit() {
        val pin = entry
        if (pin.length < 4) {
            error = "Bitte mindestens 4 Ziffern eingeben."
            return
        }
        when (mode) {
            PinMode.SETUP -> {
                if (step == 0) {
                    firstNew = pin
                    entry = ""
                    step = 1
                    error = null
                } else if (pin == firstNew) {
                    repository.setPin(pin)
                    onDone()
                } else {
                    error = "Die PINs stimmen nicht überein."
                    entry = ""
                    step = 0
                    firstNew = ""
                }
            }
            PinMode.VERIFY -> {
                if (repository.verifyPin(pin)) {
                    onDone()
                } else {
                    error = "Falsche PIN. Bitte erneut versuchen."
                    entry = ""
                }
            }
            PinMode.CHANGE -> {
                when (step) {
                    0 -> {
                        if (repository.verifyPin(pin)) {
                            entry = ""
                            step = 1
                            error = null
                        } else {
                            error = "Die alte PIN ist falsch."
                            entry = ""
                        }
                    }
                    1 -> {
                        firstNew = pin
                        entry = ""
                        step = 2
                        error = null
                    }
                    else -> {
                        if (pin == firstNew) {
                            repository.setPin(pin)
                            onDone()
                        } else {
                            error = "Die neuen PINs stimmen nicht überein."
                            entry = ""
                            step = 0
                            firstNew = ""
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        TopBar(title = "VERWALTUNG", onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontSize = fs(28),
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "•".repeat(entry.length).ifEmpty { "—" },
                fontSize = fs(36),
                fontWeight = FontWeight.Bold,
                color = BluePhone,
            )
            error?.let {
                Text(
                    text = it,
                    fontSize = fs(20),
                    fontWeight = FontWeight.Bold,
                    color = RedEmergency,
                    textAlign = TextAlign.Center,
                )
            }

            val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "LÖSCHEN", "0", "OK")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (row in 0 until 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        for (column in 0 until 3) {
                            val label = keys[row * 3 + column]
                            val color = when (label) {
                                "OK" -> GreenMessages
                                "LÖSCHEN" -> OrangeImportant
                                else -> BluePhone
                            }
                            PinKey(
                                label = label,
                                color = color,
                                onClick = {
                                    when (label) {
                                        "OK" -> submit()
                                        "LÖSCHEN" -> if (entry.isNotEmpty()) entry = entry.dropLast(1)
                                        else -> appendDigit(label)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
            Text(
                text = "Die PIN wird nur als Prüfsumme auf diesem Gerät gespeichert.",
                fontSize = fs(18),
                color = DarkText,
                textAlign = TextAlign.Center,
            )
            ThickDivider()
            BottomBackButton(onBack)
        }
    }
}

@Composable
private fun PinKey(
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White,
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        Text(
            text = label,
            fontSize = fs(24),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
