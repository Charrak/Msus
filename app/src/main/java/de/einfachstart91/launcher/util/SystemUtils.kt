package de.einfachstart91.launcher.util

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val clockFormat = SimpleDateFormat("HH:mm", Locale.GERMANY)
private val dateFormat = SimpleDateFormat("EEEE, d. MMMM", Locale.GERMANY)

fun formatClock(timeMillis: Long): String = clockFormat.format(Date(timeMillis))
fun formatDate(timeMillis: Long): String = dateFormat.format(Date(timeMillis))

data class BatteryState(
    val percent: Int = 0,
    val charging: Boolean = false,
    val present: Boolean = false,
)

@Composable
fun rememberBatteryState(): State<BatteryState> {
    val context = LocalContext.current
    return produceState(initialValue = BatteryState()) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val percent = if (level >= 0 && scale > 0) level * 100 / scale else 0
                value = BatteryState(
                    percent = percent,
                    charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL,
                    present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true),
                )
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val sticky = ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        sticky?.let { receiver.onReceive(context, it) }
        awaitDispose {
            runCatching { context.unregisterReceiver(receiver) }
        }
    }
}

@Composable
fun rememberCurrentTimeMillis(): State<Long> = produceState(initialValue = System.currentTimeMillis()) {
    while (true) {
        value = System.currentTimeMillis()
        delay(30_000)
    }
}

private fun readWifiEnabled(context: Context): Boolean = try {
    val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    manager?.isWifiEnabled == true
} catch (_: Exception) {
    false
}

@Composable
fun rememberWifiEnabled(): State<Boolean> {
    val context = LocalContext.current
    return produceState(initialValue = false) {
        while (true) {
            value = readWifiEnabled(context)
            delay(10_000)
        }
    }
}

@Composable
fun rememberContactImage(path: String?): State<ImageBitmap?> = produceState<ImageBitmap?>(
    initialValue = null,
    key1 = path,
) {
    value = withContext(Dispatchers.IO) {
        if (path.isNullOrBlank()) return@withContext null
        val file = File(path)
        if (!file.exists()) return@withContext null
        try {
            BitmapFactory.decodeFile(path)?.asImageBitmap()
        } catch (_: Exception) {
            null
        }
    }
}

fun Activity.setupLightSystemBars() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
    WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = true
        isAppearanceLightNavigationBars = true
    }
}

fun Context.hasCallPermission(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

fun Context.isDefaultHomeRole(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return false
    val roleManager = getSystemService(RoleManager::class.java) ?: return false
    return roleManager.isRoleHeld(RoleManager.ROLE_HOME)
}

fun Context.requestDefaultHomeRole(onError: (String) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = getSystemService(RoleManager::class.java)
        if (roleManager != null) {
            safeStart(
                roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME),
                Intent(Settings.ACTION_HOME_SETTINGS),
                onError,
            )
            return
        }
    }
    safeStart(Intent(Settings.ACTION_HOME_SETTINGS), null, onError)
}

fun Context.safeStart(intent: Intent, fallback: Intent? = null, onError: (String) -> Unit) {
    try {
        when {
            intent.resolveActivity(packageManager) != null -> startActivity(intent)
            fallback != null && fallback.resolveActivity(packageManager) != null -> startActivity(fallback)
            else -> onError("Diese Funktion ist auf diesem Gerät nicht verfügbar.")
        }
    } catch (_: Exception) {
        try {
            if (fallback != null && fallback.resolveActivity(packageManager) != null) {
                startActivity(fallback)
            } else {
                onError("Die zuständige App konnte nicht geöffnet werden.")
            }
        } catch (_: Exception) {
            onError("Die zuständige App konnte nicht geöffnet werden.")
        }
    }
}

private fun cleanPhoneNumber(number: String): String =
    number.trim().replace(Regex("[\\s\\-()]"), "")

fun Context.placeCall(number: String, hasCallPermission: Boolean, onError: (String) -> Unit) {
    val clean = cleanPhoneNumber(number)
    if (clean.isBlank()) {
        onError("Für diesen Kontakt ist keine Telefonnummer hinterlegt.")
        return
    }
    val call = Intent(Intent.ACTION_CALL, Uri.parse("tel:$clean"))
    val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$clean"))
    if (hasCallPermission) safeStart(call, dial, onError) else safeStart(dial, null, onError)
}

fun Context.openDialer(onError: (String) -> Unit) =
    safeStart(Intent(Intent.ACTION_DIAL), null, onError)

fun Context.openCallLog(onError: (String) -> Unit) =
    safeStart(
        Intent(Intent.ACTION_VIEW, CallLog.Calls.CONTENT_URI),
        Intent(Intent.ACTION_DIAL),
        onError,
    )

fun Context.openContacts(onError: (String) -> Unit) =
    safeStart(
        Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI),
        Intent(Intent.ACTION_DIAL),
        onError,
    )

fun Context.openDefaultSmsApp(onError: (String) -> Unit) =
    safeStart(
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING),
        Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")),
        onError,
    )

fun Context.openNewSms(onError: (String) -> Unit) =
    safeStart(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")), null, onError)

fun Context.openWhatsApp(onError: (String) -> Unit) {
    val launch = packageManager.getLaunchIntentForPackage("com.whatsapp")
        ?: packageManager.getLaunchIntentForPackage("com.whatsapp.w4b")
    if (launch == null) {
        onError("WhatsApp ist auf diesem Gerät nicht installiert.")
    } else {
        safeStart(launch, null, onError)
    }
}

fun Context.openPhotos(onError: (String) -> Unit) =
    safeStart(
        Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
        onError,
    )

fun Context.openCamera(onError: (String) -> Unit) =
    safeStart(Intent(MediaStore.ACTION_IMAGE_CAPTURE), null, onError)

fun Context.openSoundSettings(onError: (String) -> Unit) =
    safeStart(Intent(Settings.ACTION_SOUND_SETTINGS), null, onError)

fun Context.openWifiSettings(onError: (String) -> Unit) =
    safeStart(Intent(Settings.ACTION_WIFI_SETTINGS), null, onError)

fun Context.openHomeSettings(onError: (String) -> Unit) =
    safeStart(Intent(Settings.ACTION_HOME_SETTINGS), null, onError)
