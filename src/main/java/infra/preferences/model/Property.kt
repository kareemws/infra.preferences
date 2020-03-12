package infra.preferences.model

import androidx.lifecycle.Observer
import infra.preferences.contract.PreferencesFile

class Property<T> @JvmOverloads constructor(
    private val name: String,
    private val preferencesFile: PreferencesFile,
    value: T? = null,
    private val shouldStore: Boolean = false
) {

    private var isInitialized = false

    var value: T? = null
        set(value) {
            field = getValue(value)
            when (isInitialized) {
                false -> isInitialized = true
                true -> notifyObservers()
            }
        }

    var initializationValue: T? = null

    init {
        initializationValue = value
        preferencesFile.attachProperty(this)
    }

    private val observers: ArrayList<Observer<T>> = ArrayList()

    private fun getValue(value: T?): T? = when {
        shouldStore && value != null -> {
            preferencesFile.storeValue(name, value)
            value
        }
        shouldStore && value == null && !isInitialized -> {
            preferencesFile.retrieveValue(name)
        }
        shouldStore && value == null && isInitialized -> {
            preferencesFile.deleteValue(name)
            value
        }
        else -> value
    }

    fun observe(observer: Observer<T>) {
        observers.add(observer)
    }

    fun stopObserving(observer: Observer<T>) {
        observers.remove(observer)
    }

    fun init() {
        value = initializationValue
    }

    private fun notifyObservers() {
        observers.forEach {
            it.onChanged(value)
        }
    }
}