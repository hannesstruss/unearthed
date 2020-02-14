package de.hannesstruss.unearthed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gravestone(
    /** ID of the process that died */
    val pid: Int,

    /** Time at which the app went to the background. Milliseconds since epoch. */
    val backgroundedEpochMillis: Long,

    /** Milliseconds between app being sent to background and being restored. */
    val millisToRestore: Long
): Parcelable
