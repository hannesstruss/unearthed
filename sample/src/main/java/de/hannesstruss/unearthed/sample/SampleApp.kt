package de.hannesstruss.unearthed.sample

import android.app.Application
import de.hannesstruss.unearthed.Unearthed

@Suppress("unused")
class SampleApp : Application() {
  override fun onCreate() {
    super.onCreate()

    Unearthed.init(this)
  }
}
