package de.hannesstruss.unearthed

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.Date

object Unearthed {
  private val listeners = mutableSetOf<(Gravestone) -> Unit>()

  private var restored: Gravestone? = null

  fun init(app: Application) {

    app.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {
      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        outState.putString("Unearthed", "hello")
      }

      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
          val gravestone = Gravestone(Date(), Date())
          restored = gravestone
          listeners.forEach {
            it(gravestone)
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
}
