package de.hannesstruss.unearthed

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Process
import java.util.*

private const val KEY_TIME_OF_SAVE_EPOCH_MILLIS = "unearthed_time_of_save_epoch_millis"
private const val KEY_PID_AT_SAVE = "unearthed_pid_at_save"

object Unearthed {
  private val listeners = mutableSetOf<(Gravestone) -> Unit>()

  private var restored: Gravestone? = null

  internal fun init(app: Application) {
    // TODO: Handle multiple activities going into the background/being restored
    app.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {
      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        outState.putLong(KEY_TIME_OF_SAVE_EPOCH_MILLIS, nowEpochMillis())
        outState.putInt(KEY_PID_AT_SAVE, Process.myPid())
      }

      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
          val pid = savedInstanceState.getInt(KEY_PID_AT_SAVE, -1)
          val isNewProcess = pid != -1 && pid != Process.myPid()

          if (isNewProcess && restored == null) {
            val timeOfSaveEpochMillis =
              savedInstanceState.getLong(KEY_TIME_OF_SAVE_EPOCH_MILLIS)

            val gravestone = Gravestone(
              backgroundedEpochMillis = timeOfSaveEpochMillis,
              millisToRestore = nowEpochMillis() - timeOfSaveEpochMillis
            )

            restored = gravestone
            listeners.forEach {
              it(gravestone)
            }
          }
        }
      }
    })
  }

  // TODO: Naming – is it really the _process_ that's restored?
  fun onProcessRestored(callback: (Gravestone) -> Unit) {
    restored?.let { callback(it) }
    listeners.add(callback)
  }

  private fun nowEpochMillis(): Long {
    val cal = Calendar.getInstance()
    return cal.timeInMillis
  }
}
