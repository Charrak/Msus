package de.einfachstart91.launcher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.einfachstart91.launcher.R
import de.einfachstart91.launcher.data.SeniorContact
import de.einfachstart91.launcher.ui.theme.BluePhone
import de.einfachstart91.launcher.ui.theme.CardWhite
import de.einfachstart91.launcher.ui.theme.DarkText
import de.einfachstart91.launcher.ui.theme.DividerColor
import de.einfachstart91.launcher.ui.theme.GreenMessages
import de.einfachstart91.launcher.ui.theme.LightBackground
import de.einfachstart91.launcher.ui.theme.NeutralButton
import de.einfachstart91.launcher.ui.theme.OrangeImportant
import de.einfachstart91.launcher.ui.theme.RedEmergency
import de.einfachstart91.launcher.ui.theme.SoftBlue
import de.einfachstart91.launcher.ui.theme.SoftBlueDark
import de.einfachstart91.launcher.ui.theme.fs
import de.einfachstart91.launcher.util.BatteryState
import de.einfachstart91.launcher.util.rememberContactImage

@Composable
fun BigButton(
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    enabled: Boolean = true,
    minHeight: Dp = 88.dp,
    textColor: Color = Color.White,
    iconTint: Color = Color.White,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor,
            disabledContainerColor = Color(0xFFE0E0E0),
            disabledContentColor = DarkText,
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        if (iconRes != null) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconTint),
                modifier = Modifier.size(40.dp),
            )
            Spacer(Modifier.width(10.dp))
        }
        Text(
            text = label,
            fontSize = fs(22),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = fs(24),
        )
    }
}

@Composable
fun BigIconButton(
    iconRes: Int,
    accessibilityLabel: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.size(80.dp),
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.semantics { contentDescription = accessibilityLabel },
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFE0E0E0),
            disabledContentColor = DarkText,
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(40.dp),
        )
    }
}

@Composable
fun TopBar(
    title: String,
    onBack: () -> Unit,
    onSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(LightBackground)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier
                .width(140.dp)
                .height(80.dp),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BluePhone,
                contentColor = Color.White,
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(30.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text("ZURÜCK", fontSize = fs(18), fontWeight = FontWeight.Bold, maxLines = 1)
        }

        Text(
            text = title,
            fontSize = fs(22),
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
        )

        if (onSettings != null) {
            BigIconButton(
                iconRes = R.drawable.ic_settings,
                accessibilityLabel = "Einstellungen für Angehörige, PIN-geschützt",
                color = OrangeImportant,
                onClick = onSettings,
                modifier = Modifier.size(80.dp),
            )
        } else {
            Spacer(Modifier.width(140.dp))
        }
    }
}

@Composable
fun SectionHeading(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = fs(24),
        fontWeight = FontWeight.Bold,
        color = DarkText,
        modifier = modifier.padding(vertical = 10.dp),
    )
}

@Composable
fun ThickDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth().height(3.dp),
        thickness = 3.dp,
        color = DividerColor,
    )
}

@Composable
fun ContactAvatar(contact: SeniorContact?, modifier: Modifier = Modifier) {
    val bitmap by rememberContactImage(contact?.photoPath)
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(if (contact == null) SoftBlue else SoftBlueDark),
        contentAlignment = Alignment.Center,
    ) {
        val image = bitmap
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Text(
                text = contact?.initials() ?: "+",
                fontSize = fs(28),
                fontWeight = FontWeight.Bold,
                color = BluePhone,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ContactCard(
    contact: SeniorContact?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val label = if (contact != null) {
        "${contact.name.ifBlank { "Kontakt" }} anrufen"
    } else {
        "Kontakt hinzufügen, Einstellungen öffnen"
    }
    Card(
        onClick = onClick,
        modifier = modifier.semantics { contentDescription = label },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ContactAvatar(contact, Modifier.size(64.dp))
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = contact?.name?.ifBlank { "Name fehlt" } ?: "Kontakt\nhinzufügen",
                    fontSize = fs(19),
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    maxLines = 2,
                    lineHeight = fs(20),
                )
                if (contact != null) {
                    Text(
                        text = contact.relation.ifBlank { "Anrufen" },
                        fontSize = fs(14),
                        color = DarkText,
                        maxLines = 1,
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.ic_phone),
                contentDescription = null,
                colorFilter = ColorFilter.tint(if (contact == null) Color.Gray else BluePhone),
                modifier = Modifier.size(36.dp),
            )
        }
    }
}

@Composable
fun ContactInfoCard(
    contact: SeniorContact?,
    missingText: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ContactAvatar(contact, Modifier.size(84.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = contact?.name?.ifBlank { missingText } ?: missingText,
                    fontSize = fs(26),
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                )
                if (contact != null) {
                    Text(
                        text = contact.phone.ifBlank { "Keine Nummer hinterlegt" },
                        fontSize = fs(20),
                        color = DarkText,
                    )
                    if (contact.relation.isNotBlank()) {
                        Text(text = contact.relation, fontSize = fs(18), color = DarkText)
                    }
                }
            }
        }
    }
}

@Composable
fun BatteryBadge(state: BatteryState, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_battery),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                if (state.percent <= 20 && !state.charging) RedEmergency else GreenMessages,
            ),
            modifier = Modifier.size(30.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "${state.percent} %",
            fontSize = fs(20),
            fontWeight = FontWeight.Bold,
            color = DarkText,
        )
        if (state.charging) {
            Text(text = " · lädt", fontSize = fs(16), color = DarkText)
        }
    }
}

@Composable
fun BigTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = fs(18)) },
        textStyle = LocalTextStyle.current.copy(fontSize = fs(22), color = DarkText),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
    )
}

@Composable
fun BigSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconRes: Int? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardWhite)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconRes != null) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(BluePhone),
                modifier = Modifier.size(36.dp),
            )
            Spacer(Modifier.width(10.dp))
        }
        Text(
            text = label,
            fontSize = fs(20),
            fontWeight = FontWeight.Bold,
            color = DarkText,
            modifier = Modifier.weight(1f),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun DialogBigButton(
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        Text(label, fontSize = fs(20), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
fun BigConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    destructive: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(title, fontSize = fs(26), fontWeight = FontWeight.Bold, color = DarkText)
        },
        text = {
            Text(message, fontSize = fs(20), color = DarkText)
        },
        confirmButton = {
            DialogBigButton(
                label = confirmLabel,
                color = if (destructive) RedEmergency else BluePhone,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            DialogBigButton(
                label = "ABBRECHEN",
                color = NeutralButton,
                onClick = onDismiss,
            )
        },
    )
}

@Composable
fun BottomBackButton(onBack: () -> Unit, modifier: Modifier = Modifier) {
    BigButton(
        label = "ZURÜCK",
        color = NeutralButton,
        onClick = onBack,
        iconRes = R.drawable.ic_back,
        modifier = modifier,
    )
}
