package infra.preferences.contract

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import infra.preferences.model.Property
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

private const val PREFERENCES_INITIALIZATION_MESSAGE =
    "Sharedpreferences instance should be initialized"

abstract class PreferencesFile {
    abstract val fileName: String
    abstract val mode: Int

    private var sharedPreferenceInstance: SharedPreferences? = null

    private val properties: ArrayList<Property<*>> = ArrayList()

    fun initializeFile(context: Context) {
        sharedPreferenceInstance = context.getSharedPreferences(fileName, mode)
        properties.forEach {
            it.init()
        }
    }

    fun <T> retrieveValue(propertyName: String, type: Type): T? = try {
        val serializedValue = sharedPreferenceInstance!!.getString(propertyName, null)
        serializedValue?.let {
            Gson().fromJson<T>(it, type)
        }
    } catch (e: KotlinNullPointerException) {
        throw KotlinNullPointerException(PREFERENCES_INITIALIZATION_MESSAGE)
    }

    fun <T> storeValue(propertyName: String, value: T) {
        val serializedValue = Gson().toJson(value)
        try {
            with(sharedPreferenceInstance!!.edit()) {
                putString(propertyName, serializedValue)
                apply()
            }
        } catch (e: KotlinNullPointerException) {
            throw KotlinNullPointerException(PREFERENCES_INITIALIZATION_MESSAGE)
        }
    }

    fun deleteValue(propertyName: String) {
        try {
            with(sharedPreferenceInstance!!.edit()) {
                remove(propertyName)
                apply()
            }
        } catch (e: KotlinNullPointerException) {
            throw KotlinNullPointerException(PREFERENCES_INITIALIZATION_MESSAGE)
        }
    }

    fun attachProperty(property: Property<*>) {
        properties.add(property)
        if (sharedPreferenceInstance != null)
            property.init()
    }

    fun clearFile() {
        properties.forEach {
            it.value = null
        }
    }
}