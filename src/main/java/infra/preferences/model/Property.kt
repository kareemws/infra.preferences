package infra.preferences.model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer

abstract class Property<T> {

    open var value: T? = null
        set(value) {
            notifyObservers(field, value)
            field = value
        }

    private val observers: ArrayList<PropertyObserver<T>> = ArrayList()

    fun observe(lifecycle: Lifecycle?, observer: Observer<T>, vararg flags: NotificationFlag) {
        val propertyObserver = PropertyObserver(lifecycle, observer, flags)
        observers.add(propertyObserver)
    }

    fun removeObserver(observer: Observer<T>) {
        val propertyObserver = observers.find { it.observer == observer }
        observers.remove(propertyObserver)
    }

    fun notifyObservers(oldValue: T?, newValue: T?) {
        observers.forEach {
            it.notifyObserver(oldValue, newValue)
        }
    }

}