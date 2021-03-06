package de.hannesstruss.unearthed

import android.content.ComponentName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Gravestone(
    /** ID of the process that died */
    val pid: Int,

    /** Name of component which was visible when the process died */
    val componentName: ComponentName?,

    /** Time at which the activity was restored. Milliseconds since epoch. */
    val restoredAtEpochMillis: Long,

    /** Time at which the app went to the background. Milliseconds since epoch. */
    val backgroundedEpochMillis: Long,

    /** Milliseconds between app being sent to background and being restored. */
    val millisToRestore: Long
): Parcelable
