package de.hannesstruss.unearthed

data class Gravestone(
    /** Time at which the app went to the background. Milliseconds since epoch. */
    val backgroundedEpochMillis: Long,

    /** Milliseconds between app being sent to background and being restored. */
    val millisToRestore: Long
)
