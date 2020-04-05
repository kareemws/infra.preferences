package infra.preferences.model

import infra.preferences.contract.PreferencesFile
import java.lang.reflect.Type

class PersistentProperty<T>(
    private val type: Type,
    private val name: String,
    private val preferencesFile: PreferencesFile,
    value: T? = null
) : Property<T>() {

    override var value: T? = null
        set(value) {
            val actualValue = getValue(value)
            field = actualValue
            if (!isInitialized) {
                isInitialized = true
            }
            super.value = value
        }

    private var isInitialized = false

    var initializationValue: T? = null

    init {
        initializationValue = value
        preferencesFile.attachProperty(this)
    }

    private fun getValue(value: T?): T? = when {
        value != null -> {
            preferencesFile.storeValue(name, value)
            value
        }
        !isInitialized -> {
            preferencesFile.retrieveValue(name, type)
        }
        isInitialized -> {
            preferencesFile.deleteValue(name)
            value
        }
        else -> value
    }

    fun init() {
        value = initializationValue
    }
}