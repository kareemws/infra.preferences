package infra.preferences.model

enum class NotificationFlag(val predicate: (Any?, Any?) -> Boolean) {
    NULLED({ oldValue: Any?, newValue: Any? ->
        newValue == null && oldValue != null
    }),
    CHANGED({ oldValue: Any?, newValue: Any? ->
        newValue != oldValue
    }),
    INITIALIZE({ oldValue: Any?, newValue: Any? ->
        oldValue == null && newValue != null
    }),
    UPDATED({ oldValue: Any?, newValue: Any? ->
        newValue != null
    })
}