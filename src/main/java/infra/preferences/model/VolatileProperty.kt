package infra.preferences.model

class VolatileProperty<T>(value: T? = null) : Property<T>() {
    override var value: T? = null
        set(value) {
            field = value
            super.value = value
        }

    init {
        this.value = value
    }
}