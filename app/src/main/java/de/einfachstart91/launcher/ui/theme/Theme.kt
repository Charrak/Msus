package de.einfachstart91.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val BluePhone = Color(0xFF1565C0)
val GreenMessages = Color(0xFF2E7D32)
val RedEmergency = Color(0xFFC62828)
val OrangeImportant = Color(0xFFEF6C00)
val NeutralButton = Color(0xFF616161)
val LightBackground = Color(0xFFFAFAFA)
val DarkText = Color(0xFF1A1A1A)
val CardWhite = Color(0xFFFFFFFF)
val DividerColor = Color(0xFFD6D6D6)
val SoftBlue = Color(0xFFE3F2FD)
val SoftBlueDark = Color(0xFFBBDEFB)

val LocalFontScale = staticCompositionLocalOf { 1.15f }

@Composable
fun fs(base: Int): TextUnit = (base * LocalFontScale.current).sp

@Composable
fun fs(base: Float): TextUnit = (base * LocalFontScale.current).sp

@Composable
fun EinfachStartTheme(fontScale: Float, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = BluePhone,
            onPrimary = Color.White,
            background = LightBackground,
            onBackground = DarkText,
            surface = CardWhite,
            onSurface = DarkText,
            error = RedEmergency,
            onError = Color.White,
        ),
    ) {
        CompositionLocalProvider(LocalFontScale provides fontScale) {
            content()
        }
    }
}
