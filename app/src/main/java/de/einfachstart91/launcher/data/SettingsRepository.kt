package de.einfachstart91.launcher.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.security.SecureRandom

class SettingsRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _state = MutableStateFlow(load())
    val state: StateFlow<AppSettings> = _state.asStateFlow()

    private fun load(): AppSettings {
        val raw = prefs.getString(KEY_SETTINGS, null) ?: return AppSettings()
        return try {
            AppSettings.fromJson(JSONObject(raw))
        } catch (_: Exception) {
            AppSettings()
        }
    }

    private fun persist(next: AppSettings) {
        prefs.edit().putString(KEY_SETTINGS, next.toJson().toString()).apply()
        _state.value = next
    }

    private inline fun update(block: (AppSettings) -> AppSettings) {
        persist(block(_state.value))
    }

    private fun deletePhoto(path: String?) {
        if (path.isNullOrBlank()) return
        runCatching { File(path).delete() }
    }

    fun addOrUpdateContact(contact: SeniorContact) = update { current ->
        val old = current.contacts.firstOrNull { it.id == contact.id }
        if (old != null && old.photoPath != contact.photoPath) deletePhoto(old.photoPath)
        val merged = (current.contacts.filterNot { it.id == contact.id } + contact)
            .sortedBy { it.order }
            .mapIndexed { index, item -> item.copy(order = index) }
            .take(MAX_CONTACTS)
        current.copy(contacts = merged.mapIndexed { index, item -> item.copy(order = index) })
    }

    fun deleteContact(id: String) = update { current ->
        val removed = current.contacts.firstOrNull { it.id == id }
        deletePhoto(removed?.photoPath)
        val remaining = current.contacts.filterNot { it.id == id }
            .mapIndexed { index, item -> item.copy(order = index) }
        current.copy(
            contacts = remaining,
            emergencyContactId = if (current.emergencyContactId == id) null else current.emergencyContactId,
            helpContactId = if (current.helpContactId == id) null else current.helpContactId,
        )
    }

    fun moveContact(id: String, delta: Int) = update { current ->
        val list = current.contacts.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index < 0) return@update current
        val target = (index + delta).coerceIn(0, list.lastIndex)
        if (target == index) return@update current
        val item = list.removeAt(index)
        list.add(target, item)
        current.copy(contacts = list.mapIndexed { i, c -> c.copy(order = i) })
    }

    fun toggleFavorite(id: String) = update { current ->
        current.copy(contacts = current.contacts.map {
            if (it.id == id) it.copy(favorite = !it.favorite) else it
        })
    }

    fun setEmergencyContact(id: String?) = update { it.copy(emergencyContactId = id) }
    fun setHelpContact(id: String?) = update { it.copy(helpContactId = id) }
    fun setShow112(value: Boolean) = update { it.copy(show112 = value) }
    fun setWeatherText(value: String) = update { it.copy(weatherText = value.take(80)) }
    fun setFontScale(value: Float) = update { it.copy(fontScale = value.coerceIn(1.0f, 1.35f)) }
    fun setFunctionVisible(key: String, value: Boolean) = update { current ->
        val next = current.visibleFunctions.toMutableMap()
        next[key] = value
        current.copy(visibleFunctions = next)
    }

    fun hasPin(): Boolean = _state.value.pinHash != null && _state.value.pinSalt != null

    fun setPin(pin: String) {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val hash = hashPin(pin, salt)
        update {
            it.copy(
                pinSalt = Base64.encodeToString(salt, Base64.NO_WRAP),
                pinHash = hash,
            )
        }
    }

    fun verifyPin(pin: String): Boolean {
        val state = _state.value
        val expected = state.pinHash ?: return false
        val saltBase64 = state.pinSalt ?: return false
        val salt = try {
            Base64.decode(saltBase64, Base64.NO_WRAP)
        } catch (_: Exception) {
            return false
        }
        val actual = hashPin(pin, salt)
        return MessageDigest.isEqual(expected.toByteArray(), actual.toByteArray())
    }

    private fun hashPin(pin: String, salt: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(salt)
        return digest.digest(pin.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }

    suspend fun copyContactPhoto(uri: Uri, contactId: String): String? = withContext(Dispatchers.IO) {
        try {
            val directory = File(context.filesDir, "contact_photos").apply { mkdirs() }
            val target = File(directory, "$contactId.jpg")
            val bitmap = decodeSampledBitmap(uri, 512) ?: return@withContext null
            FileOutputStream(target).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
            bitmap.recycle()
            target.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    private fun decodeSampledBitmap(uri: Uri, requiredSize: Int): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, bounds)
        } ?: return null
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null
        var sample = 1
        val longest = maxOf(bounds.outWidth, bounds.outHeight)
        while (longest / (sample * 2) >= requiredSize) sample *= 2
        val options = BitmapFactory.Options().apply {
            inSampleSize = sample
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        return context.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input, null, options)
        }
    }

    companion object {
        private const val PREFS_NAME = "einfachstart91_settings"
        private const val KEY_SETTINGS = "settings_json"
        const val MAX_CONTACTS = 8
    }
}
