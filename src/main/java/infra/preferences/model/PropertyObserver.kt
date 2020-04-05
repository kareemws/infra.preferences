package infra.preferences.model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer

class PropertyObserver<T>(
    private val lifecycle: Lifecycle?,
    val observer: Observer<T>,
    private val flags: Array<out NotificationFlag>
) {

    private val validStates = arrayListOf(
        Lifecycle.State.CREATED,
        Lifecycle.State.STARTED,
        Lifecycle.State.RESUMED
    )
    private val isValidLifecycleState: Boolean
        get() = lifecycle?.let {
            validStates.contains(it.currentState)
        } ?: true

    fun notifyObserver(oldValue: T?, newValue: T?) {
        val shouldNotify =
            flags.map { it.predicate(oldValue, newValue) }.reduce { acc, b -> acc || b }
        if (shouldNotify && isValidLifecycleState) {
            observer.onChanged(newValue)
        }
    }

}