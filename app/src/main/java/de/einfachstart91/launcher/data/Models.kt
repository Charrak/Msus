package de.einfachstart91.launcher.data

import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

object FunctionKeys {
    const val PHONE = "phone"
    const val MESSAGES = "messages"
    const val WHATSAPP = "whatsapp"
    const val PHOTOS = "photos"
    const val CAMERA = "camera"
    const val HELP = "help"
    const val CONTACTS = "contacts"
    val ALL = listOf(PHONE, CONTACTS, MESSAGES, WHATSAPP, PHOTOS, CAMERA, HELP)
}

enum class PinMode { SETUP, VERIFY, CHANGE }

enum class ContactPickerMode { ADD, CALL, FILL }

private fun JSONObject.optNullableString(key: String): String? {
    val value = opt(key) ?: return null
    if (value == JSONObject.NULL) return null
    val text = value.toString()
    return text.ifEmpty { null }
}

data class SeniorContact(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val relation: String = "",
    val phone: String = "",
    val photoPath: String? = null,
    val favorite: Boolean = true,
    val order: Int = 0,
) {
    fun initials(): String {
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (parts.isEmpty()) return "?"
        return parts.take(2).joinToString("") { it.first().uppercaseChar().toString() }
    }

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("relation", relation)
        put("phone", phone)
        put("photoPath", photoPath ?: JSONObject.NULL)
        put("favorite", favorite)
        put("order", order)
    }

    companion object {
        fun fromJson(o: JSONObject): SeniorContact = SeniorContact(
            id = o.optString("id", UUID.randomUUID().toString()),
            name = o.optString("name", ""),
            relation = o.optString("relation", ""),
            phone = o.optString("phone", ""),
            photoPath = o.optNullableString("photoPath"),
            favorite = o.optBoolean("favorite", true),
            order = o.optInt("order", 0),
        )
    }
}

data class AppSettings(
    val contacts: List<SeniorContact> = emptyList(),
    val emergencyContactId: String? = null,
    val helpContactId: String? = null,
    val show112: Boolean = true,
    val weatherText: String = "",
    val fontScale: Float = 1.15f,
    val visibleFunctions: Map<String, Boolean> = FunctionKeys.ALL.associateWith { true },
    val pinSalt: String? = null,
    val pinHash: String? = null,
    val allowAddWithoutPin: Boolean = true,
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("contacts", JSONArray().also { array -> contacts.forEach { array.put(it.toJson()) } })
        put("emergencyContactId", emergencyContactId ?: JSONObject.NULL)
        put("helpContactId", helpContactId ?: JSONObject.NULL)
        put("show112", show112)
        put("weatherText", weatherText)
        put("fontScale", fontScale.toDouble())
        put("visibleFunctions", JSONObject().also { obj ->
            FunctionKeys.ALL.forEach { key -> obj.put(key, visibleFunctions[key] != false) }
        })
        put("pinSalt", pinSalt ?: JSONObject.NULL)
        put("pinHash", pinHash ?: JSONObject.NULL)
        put("allowAddWithoutPin", allowAddWithoutPin)
    }

    companion object {
        fun fromJson(o: JSONObject): AppSettings {
            val array = o.optJSONArray("contacts") ?: JSONArray()
            val contacts = (0 until array.length())
                .mapNotNull { index -> array.optJSONObject(index)?.let { SeniorContact.fromJson(it) } }
                .sortedBy { it.order }
                .mapIndexed { index, contact -> contact.copy(order = index) }
            val visObj = o.optJSONObject("visibleFunctions")
            val visible = FunctionKeys.ALL.associateWith { key -> visObj?.optBoolean(key, true) ?: true }
            return AppSettings(
                contacts = contacts,
                emergencyContactId = o.optNullableString("emergencyContactId"),
                helpContactId = o.optNullableString("helpContactId"),
                show112 = o.optBoolean("show112", true),
                weatherText = o.optString("weatherText", ""),
                fontScale = o.optDouble("fontScale", 1.15).toFloat(),
                visibleFunctions = visible,
                pinSalt = o.optNullableString("pinSalt"),
                pinHash = o.optNullableString("pinHash"),
                allowAddWithoutPin = o.optBoolean("allowAddWithoutPin", true),
            )
        }
    }
}
