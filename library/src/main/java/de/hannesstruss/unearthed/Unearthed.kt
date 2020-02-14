package de.hannesstruss.unearthed

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Process
import java.util.*

private const val KEY_TIME_OF_SAVE_EPOCH_MILLIS = "unearthed_time_of_save_epoch_millis"
private const val KEY_PID_AT_SAVE = "unearthed_pid_at_save"
private const val KEY_GRAVEYARD = "unearthed_graveyard"

// TODO: Handle multiple activities going into the background/being restored
// TODO: What happens when a restored process dies again?
class Unearthed internal constructor(private val currentPid: Int) {
  companion object {
    internal fun init(app: Application) {
      val unearthed = Unearthed(currentPid = Process.myPid())
      app.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
          unearthed.onActivitySaveInstanceState(activity, outState)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
          unearthed.onActivityCreated(savedInstanceState)
        }
      })
    }
  }

  private val listeners = mutableSetOf<(Graveyard) -> Unit>()
  private var graveyard: Graveyard? = null
  private var restored: Gravestone? = null

  internal fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    outState.putLong(KEY_TIME_OF_SAVE_EPOCH_MILLIS, nowEpochMillis())
    outState.putInt(KEY_PID_AT_SAVE, Process.myPid())
    graveyard?.let {
      outState.putParcelableArrayList(KEY_GRAVEYARD, it.gravestones.toArrayList())
    }
  }

  internal fun onActivityCreated(savedInstanceState: Bundle?) {
    if (savedInstanceState != null) {
      val pid = savedInstanceState.getInt(KEY_PID_AT_SAVE, -1)
      val isNewProcess = pid != -1 && pid != Process.myPid()

      if (isNewProcess && restored == null) {
        val timeOfSaveEpochMillis =
          savedInstanceState.getLong(KEY_TIME_OF_SAVE_EPOCH_MILLIS)

        val gravestone = Gravestone(
          pid = pid,
          backgroundedEpochMillis = timeOfSaveEpochMillis,
          millisToRestore = nowEpochMillis() - timeOfSaveEpochMillis
        )

        restored = gravestone
        val gravestones: ArrayList<Gravestone> =
          savedInstanceState.getParcelableArrayList(KEY_GRAVEYARD) ?: arrayListOf()
        gravestones.add(gravestone)
        val graveyard = Graveyard(gravestones)
        listeners.forEach {
          it(graveyard)
        }
      }
    }
  }

  // TODO: Naming – is it really the _process_ that's restored?
  fun onProcessRestored(callback: (Graveyard) -> Unit) {
    graveyard?.let { callback(it) }
    listeners.add(callback)
  }

  private fun nowEpochMillis(): Long {
    val cal = Calendar.getInstance()
    return cal.timeInMillis
  }
}
