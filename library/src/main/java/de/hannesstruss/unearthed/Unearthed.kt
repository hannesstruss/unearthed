package de.hannesstruss.unearthed

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Process
import androidx.annotation.MainThread

private const val KEY_TIME_OF_SAVE_EPOCH_MILLIS = "unearthed_time_of_save_epoch_millis"
private const val KEY_PID_AT_SAVE = "unearthed_pid_at_save"
private const val KEY_GRAVEYARD = "unearthed_graveyard"

private val WALL_CLOCK = {
  System.currentTimeMillis()
}

class Unearthed internal constructor(
  private val currentPid: Int,
  private val epochClock: () -> Long = WALL_CLOCK
) {
  companion object {
    private var instance: Unearthed? = null

    @MainThread
    internal fun init(app: Application) {
      check(instance == null) { "Unearthed was already initialized" }

      instance = Unearthed(currentPid = Process.myPid())
      app.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
          val unearthed = checkNotNull(instance)
          unearthed.onActivitySaveInstanceState(outState)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
          val unearthed = checkNotNull(instance)
          unearthed.onActivityCreated(savedInstanceState)
        }
      })
    }

    fun onProcessRestored(callback: (Graveyard) -> Unit) {
      val unearthed = checkNotNull(instance)
      unearthed.onProcessRestored(callback)
    }
  }

  private val listeners = mutableSetOf<(Graveyard) -> Unit>()
  private var graveyard: Graveyard? = null

  internal fun onActivitySaveInstanceState(outState: Bundle) {
    outState.putLong(KEY_TIME_OF_SAVE_EPOCH_MILLIS, epochClock())
    outState.putInt(KEY_PID_AT_SAVE, currentPid)
    graveyard?.let {
      outState.putParcelableArrayList(KEY_GRAVEYARD, it.gravestones.toArrayList())
    }
  }

  internal fun onActivityCreated(savedInstanceState: Bundle?) {
    val now = epochClock()

    if (savedInstanceState != null) {
      val pid = savedInstanceState.getInt(KEY_PID_AT_SAVE, -1)
      val isNewProcess = pid != -1 && pid != currentPid

      if (isNewProcess && graveyard == null) {
        val timeOfSaveEpochMillis =
          savedInstanceState.getLong(KEY_TIME_OF_SAVE_EPOCH_MILLIS)

        val gravestone = Gravestone(
          pid = pid,
          restoredAtEpochMillis = now,
          backgroundedEpochMillis = timeOfSaveEpochMillis,
          millisToRestore = now - timeOfSaveEpochMillis
        )

        val gravestones: ArrayList<Gravestone> =
          savedInstanceState.getParcelableArrayList(KEY_GRAVEYARD) ?: arrayListOf()
        gravestones.add(gravestone)
        val graveyard = Graveyard(gravestones)
        this.graveyard = graveyard
        listeners.forEach {
          it(graveyard)
        }
      }
    }
  }

  /**
   * Register a listener to get notified when the app is restored after a process death.
   *
   * This only includes scenarios in which an [Activity] is visible, sent to the
   * background, killed by the system and then restored, e.g. by the user navigating
   * to it from the recent apps switcher.
   */
  fun onProcessRestored(callback: (Graveyard) -> Unit) {
    graveyard?.let { callback(it) }
    listeners.add(callback)
  }
}
